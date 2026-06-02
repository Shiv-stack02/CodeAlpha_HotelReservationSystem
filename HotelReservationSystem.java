import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class HotelReservationSystem extends JFrame {

    private JTextField nameField;
    private JComboBox<String> roomTypeBox;
    private JTable bookingTable;
    private DefaultTableModel tableModel;

    private final String FILE_NAME = "bookings.txt";

    public HotelReservationSystem() {

        setTitle("Hotel Reservation System");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        topPanel.add(new JLabel("Customer Name:"));
        nameField = new JTextField();
        topPanel.add(nameField);

        topPanel.add(new JLabel("Room Type:"));
        roomTypeBox = new JComboBox<>(
                new String[]{"Standard - ₹1000", "Deluxe - ₹2000", "Suite - ₹4000"});
        topPanel.add(roomTypeBox);

        JButton bookBtn = new JButton("Book Room");
        JButton cancelBtn = new JButton("Cancel Booking");

        topPanel.add(bookBtn);
        topPanel.add(cancelBtn);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[]{"Customer Name", "Room Type", "Amount", "Status"}, 0);

        bookingTable = new JTable(tableModel);

        add(new JScrollPane(bookingTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        JButton paymentBtn = new JButton("Pay");
        JButton saveBtn = new JButton("Save");
        JButton loadBtn = new JButton("Load");

        bottomPanel.add(paymentBtn);
        bottomPanel.add(saveBtn);
        bottomPanel.add(loadBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        bookBtn.addActionListener(e -> bookRoom());

        cancelBtn.addActionListener(e -> cancelBooking());

        paymentBtn.addActionListener(e -> makePayment());

        saveBtn.addActionListener(e -> saveBookings());

        loadBtn.addActionListener(e -> loadBookings());

        setVisible(true);
    }

    private void bookRoom() {

        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Enter customer name!");
            return;
        }

        String room = roomTypeBox.getSelectedItem().toString();

        String amount;

        if (room.contains("Standard"))
            amount = "1000";
        else if (room.contains("Deluxe"))
            amount = "2000";
        else
            amount = "4000";

        tableModel.addRow(
                new Object[]{name, room, amount, "Pending"});

        nameField.setText("");

        JOptionPane.showMessageDialog(this,
                "Room Booked Successfully!");
    }

    private void cancelBooking() {

        int row = bookingTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select a booking first!");
            return;
        }

        tableModel.removeRow(row);

        JOptionPane.showMessageDialog(this,
                "Booking Cancelled!");
    }

    private void makePayment() {

        int row = bookingTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select booking first!");
            return;
        }

        tableModel.setValueAt("Paid", row, 3);

        JOptionPane.showMessageDialog(this,
                "Payment Successful!");
    }

    private void saveBookings() {

        try (PrintWriter pw =
                     new PrintWriter(new FileWriter(FILE_NAME))) {

            for (int i = 0; i < tableModel.getRowCount(); i++) {

                pw.println(
                        tableModel.getValueAt(i, 0) + "," +
                        tableModel.getValueAt(i, 1) + "," +
                        tableModel.getValueAt(i, 2) + "," +
                        tableModel.getValueAt(i, 3)
                );
            }

            JOptionPane.showMessageDialog(this,
                    "Bookings Saved!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBookings() {

        try {

            tableModel.setRowCount(0);

            BufferedReader br =
                    new BufferedReader(new FileReader(FILE_NAME));

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                tableModel.addRow(data);
            }

            br.close();

            JOptionPane.showMessageDialog(this,
                    "Bookings Loaded!");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "No saved file found!");
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                HotelReservationSystem::new);
    }
}