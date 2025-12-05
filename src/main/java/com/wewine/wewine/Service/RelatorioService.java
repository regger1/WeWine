package com.wewine.wewine.Service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.wewine.wewine.DTO.RelatorioFiltroDTO;
import com.wewine.wewine.DTO.RelatorioPerformanceDTO;
import com.wewine.wewine.DTO.RelatorioVendasDTO;
import com.wewine.wewine.Entity.PedidoEntity;
import com.wewine.wewine.Repository.PedidoRepository;
import com.wewine.wewine.enums.CidadeEnum;
import com.wewine.wewine.enums.StatusPedido;
import com.wewine.wewine.enums.TipoVinhoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final PedidoRepository pedidoRepository;

    public RelatorioVendasDTO getVendasPorPeriodo(RelatorioFiltroDTO filtro) {
        LocalDate dataFim = LocalDate.now();
        LocalDate dataInicio = dataFim.minusDays(filtro.getDiasPeriodo());

        List<StatusPedido> statusValidos = List.of(
                StatusPedido.ENTREGUE,
                StatusPedido.FATURADO
        );

        List<PedidoEntity> pedidos = pedidoRepository.findByStatusInAndDateBetween(
                statusValidos,
                dataInicio,
                dataFim
        );

        // Filtrar por cidade se especificado
        if (filtro.getCidade() != null) {
            pedidos = pedidos.stream()
                    .filter(p -> p.getCliente() != null &&
                                 p.getCliente().getCidade() == filtro.getCidade())
                    .toList();
        }

        BigDecimal totalVendas = pedidos.stream()
                .map(PedidoEntity::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new RelatorioVendasDTO(
                filtro.getCidade(),
                totalVendas,
                pedidos.size(),
                filtro.getDiasPeriodo()
        );
    }

    public RelatorioPerformanceDTO getPerformanceProdutos(RelatorioFiltroDTO filtro) {
        LocalDate dataFim = LocalDate.now();
        LocalDate dataInicio = dataFim.minusDays(filtro.getDiasPeriodo());

        List<StatusPedido> statusValidos = List.of(
                StatusPedido.ENTREGUE,
                StatusPedido.FATURADO
        );

        List<PedidoEntity> pedidos = pedidoRepository.findByStatusInAndDateBetween(
                statusValidos,
                dataInicio,
                dataFim
        );

        // Filtrar por cidade se especificado
        if (filtro.getCidade() != null) {
            pedidos = pedidos.stream()
                    .filter(p -> p.getCliente() != null &&
                                 p.getCliente().getCidade() == filtro.getCidade())
                    .toList();
        }

        // Filtrar por tipo de vinho se especificado
        if (filtro.getTipoVinho() != null) {
            pedidos = pedidos.stream()
                    .filter(p -> p.getItens().stream()
                            .anyMatch(item -> item.getVinho().getTipo() == filtro.getTipoVinho()))
                    .toList();
        }

        return new RelatorioPerformanceDTO(
                filtro.getTipoVinho(),
                filtro.getCidade(),
                pedidos.size(),
                filtro.getDiasPeriodo()
        );
    }

    public byte[] gerarPDFVendasPorPeriodo(RelatorioFiltroDTO filtro) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);

        // Título
        Paragraph titulo = new Paragraph("Relatório de Vendas por Período")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(titulo);

        // Data de geração
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Paragraph dataGeracao = new Paragraph("Gerado em: " + LocalDate.now().format(formatter))
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(dataGeracao);

        document.add(new Paragraph("\n"));

        // Obter dados
        RelatorioVendasDTO dados = getVendasPorPeriodo(filtro);

        // Informações do filtro
        Table tabelaFiltro = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                .useAllAvailableWidth();

        tabelaFiltro.addCell(createHeaderCell("Período:"));
        tabelaFiltro.addCell(createDataCell("Últimos " + dados.getDiasPeriodo() + " dias"));

        if (dados.getCidade() != null) {
            tabelaFiltro.addCell(createHeaderCell("Cidade:"));
            tabelaFiltro.addCell(createDataCell(formatCidade(dados.getCidade())));
        } else {
            tabelaFiltro.addCell(createHeaderCell("Cidade:"));
            tabelaFiltro.addCell(createDataCell("Todas as cidades"));
        }

        document.add(tabelaFiltro);
        document.add(new Paragraph("\n"));

        // Resultados
        Table tabelaResultados = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                .useAllAvailableWidth();

        tabelaResultados.addCell(createHeaderCell("Total de Vendas:"));
        tabelaResultados.addCell(createDataCell("R$ " + dados.getTotalVendas().toString()));

        tabelaResultados.addCell(createHeaderCell("Número de Pedidos:"));
        tabelaResultados.addCell(createDataCell(dados.getNumeroPedidos().toString()));

        document.add(tabelaResultados);

        // Rodapé
        document.add(new Paragraph("\n\n"));
        Paragraph rodape = new Paragraph("WeWine - Sistema de Gestão de Vendas")
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY);
        document.add(rodape);

        document.close();
        return baos.toByteArray();
    }

    public byte[] gerarPDFPerformanceProdutos(RelatorioFiltroDTO filtro) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);

        // Título
        Paragraph titulo = new Paragraph("Relatório de Performance de Produtos")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(titulo);

        // Data de geração
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Paragraph dataGeracao = new Paragraph("Gerado em: " + LocalDate.now().format(formatter))
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(dataGeracao);

        document.add(new Paragraph("\n"));

        // Obter dados
        RelatorioPerformanceDTO dados = getPerformanceProdutos(filtro);

        // Informações do filtro
        Table tabelaFiltro = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                .useAllAvailableWidth();

        tabelaFiltro.addCell(createHeaderCell("Período:"));
        tabelaFiltro.addCell(createDataCell("Últimos " + dados.getDiasPeriodo() + " dias"));

        if (dados.getTipoVinho() != null) {
            tabelaFiltro.addCell(createHeaderCell("Tipo de Vinho:"));
            tabelaFiltro.addCell(createDataCell(formatTipoVinho(dados.getTipoVinho())));
        } else {
            tabelaFiltro.addCell(createHeaderCell("Tipo de Vinho:"));
            tabelaFiltro.addCell(createDataCell("Todos os tipos"));
        }

        if (dados.getCidade() != null) {
            tabelaFiltro.addCell(createHeaderCell("Cidade:"));
            tabelaFiltro.addCell(createDataCell(formatCidade(dados.getCidade())));
        } else {
            tabelaFiltro.addCell(createHeaderCell("Cidade:"));
            tabelaFiltro.addCell(createDataCell("Todas as cidades"));
        }

        document.add(tabelaFiltro);
        document.add(new Paragraph("\n"));

        // Resultados
        Table tabelaResultados = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                .useAllAvailableWidth();

        tabelaResultados.addCell(createHeaderCell("Número de Pedidos:"));
        tabelaResultados.addCell(createDataCell(dados.getNumeroPedidos().toString()));

        document.add(tabelaResultados);

        // Rodapé
        document.add(new Paragraph("\n\n"));
        Paragraph rodape = new Paragraph("WeWine - Sistema de Gestão de Vendas")
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY);
        document.add(rodape);

        document.close();
        return baos.toByteArray();
    }

    private Cell createHeaderCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setBold())
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setPadding(5);
    }

    private Cell createDataCell(String text) {
        return new Cell()
                .add(new Paragraph(text))
                .setPadding(5);
    }

    private String formatCidade(CidadeEnum cidade) {
        String nome = cidade.name().replace("_", " ");
        String[] palavras = nome.split(" ");
        StringBuilder resultado = new StringBuilder();

        for (String palavra : palavras) {
            if (!palavra.isEmpty()) {
                resultado.append(Character.toUpperCase(palavra.charAt(0)))
                        .append(palavra.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return resultado.toString().trim();
    }

    private String formatTipoVinho(TipoVinhoEnum tipo) {
        String nome = tipo.name().replace("_", " ");
        String[] palavras = nome.split(" ");
        StringBuilder resultado = new StringBuilder();

        for (String palavra : palavras) {
            if (!palavra.isEmpty()) {
                resultado.append(Character.toUpperCase(palavra.charAt(0)))
                        .append(palavra.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return resultado.toString().trim();
    }
}

