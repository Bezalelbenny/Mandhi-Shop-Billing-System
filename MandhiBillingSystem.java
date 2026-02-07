import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

// Item class
class Item {
    String name;
    double price;

    Item(String name, double price) {
        this.name = name;
        this.price = price;

    }
}

// Bill calculator
class BillCalculator {
    public double calculateItemTotal(double price, int qty) {
        return price * qty;
    }

    public double calculateGST(double amount) {
        return amount * 0.05;
    }
}

public class MandhiBillingSystem extends JFrame {
    Color beige = new Color(245, 245, 220);
    Color brown = new Color(102, 51, 0);
    Color lightBrown = new Color(181, 101, 29);

    JTextField customerField, qtyField;
    JComboBox<String> itemBox;
    JTextArea billArea;

    JButton addItemBtn, generateBillBtn, clearBtn;

    HashMap<String, Item> itemMap = new HashMap<>();
    double grandTotal = 0;
    

    public MandhiBillingSystem() {

        setTitle("Mandhi Shop Billing System");
        setLayout(new FlowLayout());
        getContentPane().setBackground(beige);

        setSize(450, 500);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Items
        itemMap.put("Chicken Mandhi", new Item("Chicken Mandhi", 250));
        itemMap.put("Mutton Mandhi", new Item("Mutton Mandhi", 350));
        itemMap.put("Extra Rice", new Item("Extra Rice", 100));
        itemMap.put("Soft Drink", new Item("Soft Drink", 50));
        itemMap.put("Kandhari mandhi", new Item("Kandhari mandhi", 350));
        itemMap.put("Jellikat mandhi", new Item("Jellikat mandhi", 850));
        itemMap.put("infused shawai mandhi", new Item("infused shawai mandhi", 550));

        add(new JLabel("customer name:"));
        customerField = new JTextField(20);
        add(customerField);
        add(new JLabel("\n"));
        add(new JLabel("Select Item:"));
        itemBox = new JComboBox<>(itemMap.keySet().toArray(new String[0]));
        add(itemBox);

        add(new JLabel("Quantity:"));
        qtyField = new JTextField(5);
        add(qtyField);

        addItemBtn = new JButton("Add Item");
        generateBillBtn = new JButton("Generate Bill");
        clearBtn = new JButton("Clear");

        add(addItemBtn);
        add(generateBillBtn);
        add(clearBtn);

        billArea = new JTextArea(15, 40);
        billArea.setEditable(false);
        add(new JScrollPane(billArea));

        BillCalculator calculator = new BillCalculator();

        // Add item action
        addItemBtn.addActionListener(e -> {
            try {
                String itemName = (String) itemBox.getSelectedItem();
                int qty = Integer.parseInt(qtyField.getText());

                Item item = itemMap.get(itemName);
                double itemTotal = calculator.calculateItemTotal(item.price, qty);

                grandTotal += itemTotal;

                billArea.append(
                        item.name + "  x " + qty +
                                "  = ₹" + itemTotal + "\n");

                qtyField.setText("");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter valid quantity!");
            }
        });

        // Generate bill action
        generateBillBtn.addActionListener(e -> {
            double gst = calculator.calculateGST(grandTotal);
            double finalAmount = grandTotal + gst;

            billArea.append("\n-----------------------\n");
            billArea.append("\n--------------Bill---------\n");
            billArea.append("Subtotal: ₹" + grandTotal + "\n");
            billArea.append("GST (5%): ₹" + gst + "\n");
            billArea.append("Final Amount: ₹" + finalAmount + "\n");
            billArea.append("THANK YOU FOR CHOOSING US!");

        });

        // Clear action
        clearBtn.addActionListener(e -> {
            billArea.setText("");
            customerField.setText("");
            qtyField.setText("");
            grandTotal = 0;
        });

        setVisible(true);
        
    }

    public static void main(String[] args) {
        new MandhiBillingSystem();
    }
}
