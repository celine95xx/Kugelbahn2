module de.celineevelyn.kugelbahn {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;

    exports de.celineevelyn.kugelbahn to javafx.fxml,javafx.graphics;

    opens de.celineevelyn.kugelbahn.controller to javafx.fxml;
    exports de.celineevelyn.kugelbahn.controller to javafx.fxml;
}