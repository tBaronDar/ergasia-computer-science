package com.themisdarelis.processtool.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.themisdarelis.processtool.service.ProcessManager;

public class MainPanel extends JPanel {
    public MainPanel() {
        setBorder(new EmptyBorder(24, 32, 24, 32));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Welcome to Process Tool"));

        var startButton=new JButton("Start Process");
        startButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        var processList = ProcessManager.getCurrentProcesses();
        // δημιουργία μοντέλου πίνακα και πίνακα
        ProcessInfoTableModel tableModel = new ProcessInfoTableModel(processList);
        JTable processTable = new JTable(tableModel);
        processTable.setRowHeight(32);
        processTable.setRowSelectionAllowed(true);
        JScrollPane scrollPane = new JScrollPane(processTable);
        // me to pou valeis to scrollpane kane scroll down
        //den douleuei auto....
        SwingUtilities.invokeLater(() -> scrollToBottom(scrollPane));
        add(scrollPane);

        var textInput= new JTextField();
        textInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        add(textInput);
        startButton.addActionListener(e -> {
            String input = textInput.getText().trim();
            if (!input.isEmpty()) {
                try {
                    ProcessManager.startProcess(input);
                    textInput.setText("");
                    var newList = ProcessManager.getCurrentProcesses();
                    tableModel.refreshData(newList);
                    SwingUtilities.invokeLater(() -> scrollToBottom(scrollPane));
                    JOptionPane.showMessageDialog(this, "Process started: " + input);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error starting process: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please the name of a gui program.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        add(startButton);

        var killButton=new JButton("Kill Process");
        killButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

        killButton.addActionListener(e -> {
            int selectedRow = processTable.getSelectedRow();
            if (selectedRow != -1) {
                long pid = (long) tableModel.getValueAt(selectedRow, 0);
                try {
                    ProcessManager.killProcess(pid);
                    var newList = ProcessManager.getCurrentProcesses();
                    tableModel.refreshData(newList);
                    SwingUtilities.invokeLater(() -> scrollToBottom(scrollPane));
                    JOptionPane.showMessageDialog(this, "Process killed: " + pid);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error killing process: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a program from the list.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        add(killButton);

    }

    private void scrollToBottom(JScrollPane scrollPane) {
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setValue(verticalBar.getMaximum());
    }

}
