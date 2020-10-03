package com.export;

import java.io.File;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.pojo.Trade;

public class FileWriter {
	public void CreateTable(ArrayList<ArrayList<Trade>> arr) {
		Document document = new Document();
		XSSFWorkbook workbook = new XSSFWorkbook();

		PdfWriter writer;

		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream("./FrontRunningScenarios.pdf"));
			document.open();

			Image image;
			image = Image.getInstance("./citi.png");
			image.setAbsolutePosition(500, 790);
			image.scalePercent(14f, 14f);

//			Image team_logo = Image.getInstance("filename");
//			team_logo.scalePercent(20f, 20f);
//			team_logo.setAbsolutePosition(250, 450);

			Paragraph ph = new Paragraph(new Phrase(10f, "Surveillance Report - Front Running",
					FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18f)));

			PdfPCell cell = new PdfPCell(ph);
			cell.setBorderColorBottom(new BaseColor(44, 67, 144));
			cell.setBorderWidthBottom(2f);
			cell.disableBorderSide(Rectangle.RIGHT);
			cell.disableBorderSide(Rectangle.LEFT);
			cell.disableBorderSide(Rectangle.TOP);
			cell.setMinimumHeight(50);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPTable tbl = new PdfPTable(1);
			tbl.addCell(cell);
			tbl.setHorizontalAlignment(Element.ALIGN_MIDDLE);
			tbl.setWidthPercentage(100);

			Font conf = FontFactory.getFont(FontFactory.COURIER_OBLIQUE);
			conf.setColor(BaseColor.RED);
			Font scnf = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14f);

			XSSFSheet spreadsheet = workbook.createSheet(" Front Running Scenarios ");

//			System.out.println(arr.size());
			// "Trade ID","Execution Time", "Customer ID","Security","Market Price","Order
			// Price","Order type","Quantity","Broker Name"

			float sizes[] = { 20f, 40f, 20f, 30f, 25f, 25f, 20f, 20f, 20f };
			int count = 0;
			for (int i = 0; i < arr.size(); i++) {
				document.add(tbl);
				writer.getDirectContent().addImage(image, true);
//				writer.getDirectContent().addImage(team_logo, true);

				int num = i + 1;

				Paragraph scn = new Paragraph(new Phrase(22f, "Scenario " + num, scnf));
				scn.setLeading(32f);
				document.add(scn);

				PdfPTable table = new PdfPTable(9);

				table.setWidths(sizes);
				table.setSpacingBefore(30);
				addTableHeader(table);
				int j = 0;
				for (j = 0; j < arr.get(i).size(); j++) {
					XSSFRow row;
					row = spreadsheet.createRow(j + count);

					addExcelRows(row, arr.get(i).get(j));
					addRows(table, arr.get(i).get(j));
				}
				count += j;
				spreadsheet.createRow(++count);

				document.add(table);
				ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
						new Phrase("Confidential", conf), 110, 30, 0);
				ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
						new Phrase(num + "/" + arr.size()), 550, 30, 0);
				document.newPage();

			}
			FileOutputStream out = new FileOutputStream(new File("./FrontRunning.xlsx"));

			try {
				workbook.write(out);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			document.close();
			writer.close();
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addTableHeader(PdfPTable table) {
		Stream.of("Trade ID", "Execution Time", "Cust ID", "Security", "Market Price", "Order Price", "Order type",
				"QTY.", "Brok Name").forEach(columnTitle -> {
					PdfPCell header = new PdfPCell();
					header.setBackgroundColor(BaseColor.LIGHT_GRAY);
					header.setBorderWidth(2);
					header.setPhrase(new Phrase(columnTitle));
					table.addCell(header);
				});
	}

	public void addExcelRows(XSSFRow row, Trade t) {
		int cellid = 0;
		Cell cell = row.createCell(cellid++);
		cell.setCellValue(Integer.toString(t.getTradeId()));
		cell = row.createCell(cellid++);
		cell.setCellValue(t.getTradeExecutionTime().getTime().toString());
		cell = row.createCell(cellid++);
		cell.setCellValue((Integer.toString(t.getCustomerId())));
		cell = row.createCell(cellid++);
		cell.setCellValue(t.getSecurityName());
		cell = row.createCell(cellid++);
		String MP = String.format("%.3f", t.getMarketPrice());
		cell.setCellValue(MP);
		;
		cell = row.createCell(cellid++);
		String OP = String.format("%.3f", t.getPrice());
		cell.setCellValue(OP);
		cell = row.createCell(cellid++);
		cell.setCellValue(t.isTradeType() ? "buy" : "sell");
		cell = row.createCell(cellid++);
		cell.setCellValue(Integer.toString(t.getQuantity()));
		cell = row.createCell(cellid++);
		cell.setCellValue(t.getBrokerName());

	}

	public void addRows(PdfPTable table, Trade t) {
		table.addCell(Integer.toString(t.getTradeId()));
		table.addCell(t.getTradeExecutionTime().getTime().toString());
		table.addCell(Integer.toString(t.getCustomerId()));
		table.addCell(t.getSecurityName());
		String MP = String.format("%.3f", t.getMarketPrice());
		table.addCell(MP);
		String OP = String.format("%.3f", t.getPrice());

		table.addCell(OP);
		table.addCell(t.isTradeType() ? "buy" : "sell");
		table.addCell(Integer.toString(t.getQuantity()));
		table.addCell(t.getBrokerName());

	}
}
