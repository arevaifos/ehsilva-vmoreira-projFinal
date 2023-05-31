package pt.uc.dei.helpers;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import pt.uc.dei.backingbeans.Login;


public class CSVGenerator {

	private static final File rootPath = new File("data/relatorios");
	final static Logger logger = Logger.getLogger(CSVGenerator.class);

	private ReportAllocation reportAllocation=new ReportAllocation();

	public CSVGenerator() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String generateAllocationReport(String fileName, List<ReportAllocation> reports) {
		if (!rootPath.exists()) {
			rootPath.mkdirs();
		}


		try {

			//				FileWriter writer = new FileWriter("rootPath.getPath()" + "/" + fileName);
			FileWriter writer = new FileWriter("C:\\Users\\cnest\\Desktop\\projeto final\\"+fileName);
			String input=new String();
			input=reportAllocation.toStringCabecalho();


			input = input.substring(1, input.length() - 1);
			String[] split = input.split(";");		       

			for(String s : split) {
				String[] split2 = s.split(",");
				writer.write(Arrays.asList(split2).stream().collect(Collectors.joining(",")));
				writer.write("\n"); // newline
			}


			for(int i=0; i<reports.size();i++){
				input=reports.get(i).toStringCorpo();
				input = input.substring(1, input.length() - 1);
				split = input.split(";");		       

				for(String s : split) {
					String[] split2 = s.split(",");
					writer.write(Arrays.asList(split2).stream().collect(Collectors.joining(",")));
					writer.write("\n"); // newline
				}
			}
			writer.close();


			//gerarCsv(fileName);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return rootPath.getPath() + "/" + fileName;
	}





	private void gerarCsv(String fileName) {
		String dado = new String();
		try {
			File ficheiro = new File(rootPath.getPath() + "/" + fileName);
			BufferedWriter writerBuff = new BufferedWriter(new FileWriter(ficheiro));
			String input = "[\"user1\",\"track1\",\"player1\", \"user1\",\"track2\",\"player2\", \"user1\",\"track3\",\"player3\"]";
			input = input.substring(1, input.length() - 1); // get rid of brackets
			String[] split = input.split(" ");

			//			        FileWriter writer = new FileWriter("/Users/artur/tmp/csv/sto1.csv");
			FileWriter writer = new FileWriter("C:\\Users\\cnest\\Desktop\\projeto final\\"+fileName);

			for(String s : split) {
				String[] split2 = s.split(",");
				writer.write(Arrays.asList(split2).stream().collect(Collectors.joining(",")));
				writer.write("\n"); // newline
			}

			writer.close();







			//				for (int i = 0; i < dados.length; i++) {
			//					for (int j = 0; j <= nColunas; j++) {
			//						if (j == nColunas) {
			//							writer.newLine();
			//							System.out.println("\t");
			//							writer.flush();
			//						} else {
			//							dado = dados[i][j];
			//							System.out.println(dado + ",");
			//							writer.append(dado);
			//							writer.append(',');
			//							writer.flush();
			//						}
			//
			//					}
			//				}
			//				writerBuff.newLine();
			//				writerBuff.newLine();
			//				writerbuff.newLine();
			//				writerBuff.newLine();
			//				writerBuff.write("Arquivo gravado em : " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()));
			//				writerBuff.flush();
			//				writerBuff.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
