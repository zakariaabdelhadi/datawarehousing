import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//
//
//// M�thodes utiles
////	String.split("") : d�couper une chaine de caract�res
////	String.concat("") : concat�nation de deux chaines de caract�res
////	String.toLowerCase() : convertir la chaine de caract�res en minuscule
////	String.replace("a", "b") : remplacer le caract�re "a" par "b" dans une chaine de caract�res
////	String.trim() : enlever les espaces qui se trouvent au d�but et � la fin d'une chaine de caract�re
////	String.startsWith("abc") : v�rifier si la chaine de caract�res commence par la suite de caract�res "abc"
//
//
public class PopulateFromCSV {

	void populateTable(String csvFile, String tableName, String tableSchema) throws SQLException {

		// Connexion � la base de donn�es
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/MyBase", "postgres",
				"root");
		Statement statement = connection.createStatement();
		System.out.println("Connected to database");

		// Configuration du fichier CSV
		String line = "";
		String cvsSplitBy = ",";

		// R�cup�ration des types des attributs
		String[] att = tableSchema.split(",");

		//		// hadi bach ybanoulek el omorat kda w chwiyya omorat
		//		System.out.println("tabledchema fiha -->" + tableSchema);
		//		for (String string : att) {
		//
		//			System.out.println(string);
		//		}

		List<String> attributesTypes = new ArrayList<String>();
		for (String val : att) {
			String[] tmp = val.trim().split("\\s+");// split("\\s+") --> split a string with any whitespace chars
			if (tmp.length > 1)
				attributesTypes.add(tmp[1]);
			else
				System.out.println("Attribute syntax error: " + tmp[1]);
		}

		// // afficher les types
		// for (String string : attributesTypes) {
		// System.out.println(string);
		// }
		// 7etta ellahna derna les types ta3 les attributes f arrayList

		// Cr�ation de la table dans la BD
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " (" + tableSchema + ");");
		System.out.println("Table " + tableName + " created");

		// Ouverture du fichier CSV
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

			// Sauter l'en-t�te
			br.readLine();

			// Lecture du fichier CSV ligne par ligne
			// creation de la requette
			while ((line = br.readLine()) != null) { // l objet 'br' est le fichier en personne hhh
				String query;
				query = "insert into " + tableName + " values('";

				String[] values = line.split(cvsSplitBy); // le separateur est la vergule la ligne dernaha f tableau
				for (int i = 0; i < values.length - 1; i++) {
					// System.out.println(values[i]);
					query += values[i] + "','";

				}
				query = query + values[values.length - 1] + "');";

				// TODO
				// R�cup�rer les valeurs des attributs pr�sentes
				// dans "line" et construire une requ�te "INSERT"
				// pour les ins�rer dans la table.

				System.out.println(query);
				statement.executeUpdate(query);
			}

			System.out.println("Insertion completed");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws SQLException {

		PopulateFromCSV p1 = new PopulateFromCSV();

		p1.populateTable("C:\\Users\\ZAKARIA\\Desktop\\M1-S2\\entrepotdd\\page1.csv", "vendredi",
				"OrderID char(9) PRIMARY KEY, CustomerID varchar, ItemType varchar, SalesChannel varchar, OrderPriority varchar, OrderDate varchar, ShipDate varchar, UnitsSold int, UnitePrice float, UnitCost float, TotalRevenu float, TotalCost float, TotalProfit float");
		// p1.populateTable("Chemin du fichier CSV", "Customers", "attributs de la table
		// Customers");

	}

}
