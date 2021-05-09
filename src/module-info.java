module Kugelbahn 
{
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.graphics;
	
	opens de.celineevelyn.kugelbahn.controller to javafx.graphics, javafx.fxml;
	
	exports de.celineevelyn.kugelbahn.controller to javafx.fxml;
	exports de.celineevelyn.kugelbahn to javafx.fxml, javafx.graphics;
	
}
