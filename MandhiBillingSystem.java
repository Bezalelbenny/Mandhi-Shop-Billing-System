import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class MandhiBillingSystem extends JFrame {

    JTextField customerField, qtyField;
    JComboBox<String> itemBox;
    JTextArea billArea;

    double grandTotal = 0;

    // DATABASE CONNECTION METHOD
    public Connection connectDB() {

        Connection conn = null;

        try {

            Class.forName("org.sqlite.JDBC");

            String url = "jdbc:sqlite:mandhi.db";

            conn = DriverManager.getConnection(url);

            Statement stmt = conn.createStatement();

            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS bills (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "customer TEXT," +
                            "item TEXT," +
                            "quantity INTEGER," +
                            "price REAL," +
                            "gst REAL," +
                            "final REAL)");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return conn;
    }

    // CONSTRUCTOR
    MandhiBillingSystem() {

        setTitle("Mandhi Shop Billing System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Beige background
        getContentPane().setBackground(new Color(245, 235, 220));

        // COMPONENTS
        JLabel customerLabel = new JLabel("Customer Name:");
        customerField = new JTextField(15);

        JLabel itemLabel = new JLabel("Select Item:");

        String items[] = {
                "Chicken Mandhi - 250",
                "Mutton Mandhi - 350",
                "Al Faham - 200",
                "Pepsi - 50"
        };

        itemBox = new JComboBox<>(items);

        JLabel qtyLabel = new JLabel("Quantity:");
        qtyField = new JTextField(5);

        JButton addButton = new JButton("Add Item");
        JButton billButton = new JButton("Generate Bill");
        JButton clearButton = new JButton("Clear");

        billArea = new JTextArea(20, 40);
        billArea.setEditable(false);

        JScrollPane scroll = new JScrollPane(billArea);

        // ADD COMPONENTS
        add(customerLabel);
        add(customerField);

        add(itemLabel);
        add(itemBox);

        add(qtyLabel);
        add(qtyField);

        add(addButton);
        add(billButton);
        add(clearButton);

        add(scroll);

        // ADD ITEM BUTTON
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                try {

                    String item = itemBox.getSelectedItem().toString();

                    int qty = Integer.parseInt(qtyField.getText());

                    double price = Double.parseDouble(
                            item.split("-")[1].trim());

                    double total = price * qty;

                    grandTotal = grandTotal + total;

                    billArea.append(
                            item + " x " + qty +
                                    " = " + total + "\n");

                }

                catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Enter valid quantity");

                }

            }

        });

        // GENERATE BILL BUTTON
        billButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                try {

                    double gst = grandTotal * 0.05;

                    double finalAmount = grandTotal + gst;

                    billArea.append("\n------------------\n");
                    billArea.append(
                            "Subtotal: " + grandTotal + "\n");

                    billArea.append(
                            "GST (5%): " + gst + "\n");

                    billArea.append(
                            "Final Amount: " +
                                    finalAmount + "\n");

                    // SAVE TO DATABASE
                    Connection conn = connectDB();

                    String query = "INSERT INTO bills " +
                            "(customer,item,quantity,price,gst,final) " +
                            "VALUES (?,?,?,?,?,?)";

                    PreparedStatement pst = conn.prepareStatement(query);

                    pst.setString(
                            1,
                            customerField.getText());

                    pst.setString(
                            2,
                            itemBox.getSelectedItem().toString());

                    pst.setInt(
                            3,
                            Integer.parseInt(qtyField.getText()));

                    pst.setDouble(
                            4,
                            grandTotal);

                    pst.setDouble(
                            5,
                            gst);

                    pst.setDouble(
                            6,
                            finalAmount);

                    pst.executeUpdate();

                    conn.close();

                    JOptionPane.showMessageDialog(
                            null,
                            "Bill Saved in SQLite Database");

                }

                catch (Exception ex) {

                    ex.printStackTrace();

                }

            }

        });

        // CLEAR BUTTON
        clearButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                customerField.setText("");
                qtyField.setText("");
                billArea.setText("");
                grandTotal = 0;

            }

        });

        setVisible(true);
    }

    // MAIN METHOD
    public static void main(String[] args) {

        new MandhiBillingSystem();

    }

}
