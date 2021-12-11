package bsu.rfe.java.group7.lab2.Ramnenok.varС1;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class GornerTableCellRenderer implements TableCellRenderer
{
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();

    private String needle = null;
    private DecimalFormat formatter = (DecimalFormat)NumberFormat.getInstance();

    private boolean FractionalPart(String number){
        String[] str = number.split("\\.");
        if(str.length==2) {
            int n1 = str[0].length();
            int n2 = str[1].length();
            int N1 = Integer.valueOf(str[0]);
            int N2 = Integer.valueOf(str[1]);
            int a1 = 0, a2 = 0;
            int k1 = 0, k2 = 0;
            for (int i = 0; i < n1; i++) {
                a1 = N1 % 10;
                if (a1 % 2 == 0)
                    k1++;
                N1 = N1 / 10;
            }
            for (int i = 0; i < n2; i++) {
                a2 = N2 % 10;
                if (a2 % 2 == 0)
                    k2++;
                N2 = N2 / 10;
            }
            if (k1 == n1 && k2 == n2)
                return true;
        }
        return false;
    }

    public GornerTableCellRenderer() {
        formatter.setMaximumFractionDigits(5);
        formatter.setGroupingUsed(false);
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
        panel.add(label);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
// Преобразовать double в строку с помощью форматировщика
        String formattedType = formatter.format(value);
// Установить текст надписи равным строковому представлению числа
        label.setText(formattedType);
        if (needle!=null && needle.equals(formattedType)) {
// Номер столбца = 1 (т.е. второй столбец) + иголка не null
// (значит что-то ищем) +
// значение иголки совпадает со значением ячейки таблицы -
// окрасить задний фон панели в красный цвет
            panel.setBackground(Color.RED);
        } else if(FractionalPart(formattedType)){
            panel.setBackground(Color.GREEN);
        }else{
// Иначе - в обычный белый
            panel.setBackground(Color.WHITE);
        }
        return panel;
    }
    public void setNeedle(String needle) {
        this.needle = needle;
    }
}
