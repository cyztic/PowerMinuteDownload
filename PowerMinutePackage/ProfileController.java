package PowerMinutePackage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController  implements Initializable {
    //*********************************MEMBER VARIABLES******************************************
    //get database connector singleton object for this class
    DBConnector db_connector = DBConnector.getInstance();

    //********************************GLOBAL FXML CONTROLS******************************************
    @FXML
    Label user_id_label, user_name_label, partner_id_label, user_wellbucks_label;
    @FXML
    TextField add_partner_textfield;


    //*********************************FXML METHODS******************************************
    // INPUT:    nothing
    // TASK:     ON ACTION FOR partner_today_button
    //           adds a partner to users database info
    //           by entering an ID. And updates Piechart.
    // OUTPUT:   nothing
    @FXML
    private void addPartner() {
        if (!add_partner_textfield.getText().isEmpty()) {

            //get id from text field
            int id = Integer.parseInt(add_partner_textfield.getText());

            //set it to partner in database with db connector
            db_connector.addAccountabilityPartner(id);
            partner_id_label.setText("Partner's ID: " + id);
            partner_id_label.setAlignment(Pos.CENTER);
            add_partner_textfield.setText("");
        }
    }

    // INPUT:   url and Resource Bundle
    // TASK:    called when class is started, this makes it where we
    //          can call functions and perform actions when the controller
    //          is first called.
    // OUTPUT:  none
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set name label
        user_name_label.setText(db_connector.getUsersFirstName() + " " + db_connector.getUsersLastName());
        user_name_label.setAlignment(Pos.CENTER);

        //set user well bucks
        int wellbucks = 0;
        int powerMinutes = db_connector.getUserWeeklyAccepted();
        //check if its modulo 5, if not then subtract the remainder
        if( 0 != (powerMinutes % 5))
            powerMinutes = powerMinutes - (powerMinutes % 5);
        //calculate wellbucks
        wellbucks = (powerMinutes / 5) * 1000;

        user_wellbucks_label.setText("Wellbucks earned: " + wellbucks);
        user_wellbucks_label.setAlignment(Pos.CENTER);

        //set id label to users ID
        String idLabelString;
        String idString = Integer.toString(db_connector.getUSER_ID());
        idLabelString = "Your ID: " + idString;
        user_id_label.setText(idLabelString);
        user_id_label.setAlignment(Pos.CENTER);

        //set partner id label
        if(db_connector.getPartnerID() > 0) {
            idString = Integer.toString(db_connector.getPartnerID());
            idLabelString = "Partner's ID: " + idString;
            partner_id_label.setText(idLabelString);
            partner_id_label.setAlignment(Pos.CENTER);
        } else{
            partner_id_label.setText("Partner's ID: None");
            partner_id_label.setAlignment(Pos.CENTER);
        }
    }
}
