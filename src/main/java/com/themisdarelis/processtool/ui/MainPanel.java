package com.themisdarelis.processtool.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.Desktop;
import java.net.URI;
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
        descriptionPanel.setMinimumSize(new Dimension(300, 0));
        descriptionPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
        descriptionPanel.setPreferredSize(new Dimension(350, 0));

        // Multi-line text area with wrapping
        JTextArea descLine1 = new JTextArea("Γεια σας Κυριε Μαργαριτη. \n\nΑυτό το πρόγραμμα διαχειρίζεται τις διεργασίες του υπολογιστή. \nΓραψτε το όνομα ενος προγράματος στο πεδίο κειμένου παρακάτω και πατήστε 'Start Process' για να το ξεκινήσετε. \nΕπιλέξτε μια διεργασία από τον πίνακα και πατήστε 'Kill Process' για να την τερματίσετε.\n\nΓια καλυτερα αποτελεσματα, χρησιμοποιηστε ονοματα GUI προγραμματων οπως: \n'explorer'(windows), 'firefox', 'dolphin'(kde), code(για το VSCode) κτλ.\n\nΠερισοτερες πληροφοριες στο GitHub λινκ (README) παρακατω.\n\nΣημειωση: προγραμματα οπως το VScode ανοιγουν πολλα processes, οποτε αν κλεισετε το ενα απο αυτα, το πρόγραμμα δεν θα σταματησει να λειτουργει.");
        descLine1.setLineWrap(true);
        descLine1.setWrapStyleWord(true);
        descLine1.setEditable(false);
        descLine1.setOpaque(false);
        descLine1.setFont(descLine1.getFont().deriveFont(18f));
        descLine1.setAlignmentX(Component.LEFT_ALIGNMENT);
        descLine1.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel descLine2 = new JLabel("Author: Themis Darelis");
        descLine2.setAlignmentX(Component.LEFT_ALIGNMENT);

        // GitHub link label
        JLabel githubLabel = new JLabel("GitHub: https://github.com/tBaronDar/ergsia-computer-science");
        githubLabel.setForeground(Color.BLUE);
        githubLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        githubLabel.setFont(githubLabel.getFont().deriveFont(14f));
        githubLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        githubLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                try {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().browse(new URI("https://github.com/tBaronDar/ergsia-computer-science"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        descriptionPanel.add(descLine1);
        descriptionPanel.add(Box.createRigidArea(new Dimension(0,6)));
        descriptionPanel.add(descLine2);
        descriptionPanel.add(Box.createRigidArea(new Dimension(0,6)));
        descriptionPanel.add(githubLabel);

        // pinakas kai modelo
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
        var refreshButton = new JButton("Refresh");
        var exitButton = new JButton("Exit");
        row2.add(refreshButton);
        row2.add(exitButton);

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
                    //xekina to process pou evale o xristis
                    ProcessManager.startProcess(input);
                    // katharise to input field
                    textInput.setText("");
                    // refresh ton pinaka me ta nea process
                    var newList = ProcessManager.getCurrentProcesses();
                    tableModel.refreshData(newList);
                    //kane scroll sto telos
                    SwingUtilities.invokeLater(() -> scrollToBottom(scrollPane));
                    //peta kai ena minima
                    JOptionPane.showMessageDialog(this, "Process started: " + input);
                    // epelexe to teleutaio process
                    int lastRow = processTable.getRowCount() - 1;
                    if (lastRow >= 0) {
                        processTable.setRowSelectionInterval(lastRow, lastRow);
                        processTable.scrollRectToVisible(processTable.getCellRect(lastRow, 0, true));
                    }


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

        refreshButton.addActionListener(e -> {  
            var newList = ProcessManager.getCurrentProcesses();
            tableModel.refreshData(newList);
            SwingUtilities.invokeLater(() -> scrollToBottom(scrollPane));
        });

        exitButton.addActionListener(e -> {
            System.exit(0);
        });
    }

    // kane scroll sto telos tou pinaka
    private void scrollToBottom(JScrollPane scrollPane) {
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setValue(verticalBar.getMaximum());
    }

}
