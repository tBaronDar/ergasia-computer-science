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
        this.add(scrollPane);

        var textInput= new JTextField();
        textInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        add(textInput);
        startButton.addActionListener(e -> {
            String input = textInput.getText().trim();
            if (!input.isEmpty()) {
                try {
                    ProcessManager.startProcess(input);
                    // JOptionPane.showMessageDialog(this, "Process started: " + input);

                    textInput.setText("");
                    var newList = ProcessManager.getCurrentProcesses();
                    tableModel.refreshData(newList);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error starting process: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a command to start.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        add(startButton);

        var killButton=new JButton("Kill Process");
        killButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        add(killButton);



    }

}
