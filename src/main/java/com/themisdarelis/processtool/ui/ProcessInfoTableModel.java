package com.themisdarelis.processtool.ui;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import com.themisdarelis.processtool.model.ProcessInfo;

public class ProcessInfoTableModel extends AbstractTableModel {
    private final List<ProcessInfo> processes;
    private final String[] columns = {"PID","User","Command" };

    public ProcessInfoTableModel(List<ProcessInfo> processes) {
        this.processes = processes;
    }

    @Override
    public int getRowCount() {
        return processes.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        ProcessInfo info = processes.get(row);
        switch (col) {
            case 0: return info.getPid();
            case 1: return info.getUser();
            case 2: return info.getCommand();
            default: return null;
        }
    }

    public void refreshData(List<ProcessInfo> newProcesses) {
        processes.clear();
        processes.addAll(newProcesses);
        fireTableDataChanged();
    }
}
