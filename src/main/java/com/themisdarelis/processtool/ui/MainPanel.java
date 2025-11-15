package com.themisdarelis.processtool.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.themisdarelis.processtool.service.ProcessManager;

public class MainPanel extends JPanel {
    public MainPanel() {
        setBorder(new EmptyBorder(24, 32, 24, 32));
        setLayout(new BorderLayout(12, 12));

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome to Process Tool");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 32f));
        add(welcomeLabel, BorderLayout.NORTH);

        // Keimena
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
        descriptionPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        JLabel descLine1 = new JLabel("Περιγραφή: Αυτό το πρόγραμμα διαχειρίζεται GUI διεργασίες. Γραψτε το όνομα μιας GUI διεργασίας στο πεδίο κειμένου παρακάτω και πατήστε 'Start Process' για να την ξεκινήσετε. Επιλέξτε μια διεργασία από τον πίνακα και πατήστε 'Kill Process' για να την τερματίσετε.");
        descLine1.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel descLine2 = new JLabel("Author: Themis Darelis");
        descLine2.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel githubLabel = new JLabel("<html>GitHub: <a href='https://github.com/tBaronDar/ergsia-computer-science'>https://github.com/user/repo</a></html>");
        githubLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionPanel.add(descLine1);
        descriptionPanel.add(Box.createRigidArea(new Dimension(0,6)));
        descriptionPanel.add(descLine2);
        descriptionPanel.add(Box.createRigidArea(new Dimension(0,6)));
        descriptionPanel.add(githubLabel);

        // pinaka kai modelo
        var processList = ProcessManager.getCurrentProcesses();
        ProcessInfoTableModel tableModel = new ProcessInfoTableModel(processList);
        JTable processTable = new JTable(tableModel);
        processTable.setRowHeight(32);
        processTable.setRowSelectionAllowed(true);
        processTable.setPreferredScrollableViewportSize(new Dimension(500, 500));
        processTable.setMinimumSize(new Dimension(500, 500));

        JScrollPane scrollPane = new JScrollPane(processTable);
        scrollPane.setMinimumSize(new Dimension(500, 500));

        // split panel aristera ta keimena deksia o pinakas
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, descriptionPanel, scrollPane);
        split.setResizeWeight(0.25);
        split.setOneTouchExpandable(true);
        add(split, BorderLayout.CENTER);

        // kato panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(new EmptyBorder(8, 0, 0, 0));

        // input
        var textInput = new JTextField();
        textInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        textInput.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomPanel.add(textInput);

        bottomPanel.add(Box.createRigidArea(new Dimension(0,8)));

        // 2 rows gia koubia
        JPanel buttonRows = new JPanel();
        buttonRows.setLayout(new GridLayout(2, 1, 6, 6));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        var startButton = new JButton("Start Process");
        var killButton = new JButton("Kill Process");
        row1.add(startButton);
        row1.add(killButton);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        var refreshPlaceholder = new JButton("Refresh");
        refreshPlaceholder.setEnabled(false);
        var exitPlaceholder = new JButton("Exit");
        exitPlaceholder.setEnabled(false);
        row2.add(refreshPlaceholder);
        row2.add(exitPlaceholder);

        buttonRows.add(row1);
        buttonRows.add(row2);
        buttonRows.setAlignmentX(Component.LEFT_ALIGNMENT);

        bottomPanel.add(buttonRows);

        add(bottomPanel, BorderLayout.SOUTH);

        // Scroll to bottom, distihos auto den douleuei on start up
        //den xero giati...
        SwingUtilities.invokeLater(() -> scrollToBottom(scrollPane));

        // listeners
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

    }

    private void scrollToBottom(JScrollPane scrollPane) {
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setValue(verticalBar.getMaximum());
    }

}
