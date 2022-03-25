module Kugelbahn {
	exports application;
	exports de.celineevelyn.kugelbahn.controller;
	exports de.celineevelyn.kugelbahn.objects;
	exports de.celineevelyn.kugelbahn;
	
	opens de.celineevelyn.kugelbahn.controller to javafx.graphics, javafx.fxml;

	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires commons.math3;
}