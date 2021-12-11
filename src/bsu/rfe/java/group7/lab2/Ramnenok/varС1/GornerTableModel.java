package bsu.rfe.java.group7.lab2.Ramnenok.varС1;

import javax.swing.table.AbstractTableModel;

public class GornerTableModel extends AbstractTableModel {
    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;

    public GornerTableModel(Double from, Double to, Double step, Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }

    public Double getFrom() {
        return from;
    }
    public Double getTo() {
        return to;
    }
    public Double getStep() {
        return step;
    }
    public int getColumnCount() {
        return 3;
    }
    public int getRowCount() {
        return new Double(Math.ceil((to-from)/step)).intValue()+1;
    }

    public Object getValueAt(int row, int col) {
// Вычислить значение X как НАЧАЛО_ОТРЕЗКА + ШАГ*НОМЕР_СТРОКИ
        double x = from + step * row;

        double result = coefficients[0];
        for (int i = 1; i < coefficients.length; ++i)
            result = result * x + coefficients[i];


        switch(col) {
            case 0: {
                return x;
            }
            // Если запрашивается значение 2-го столбца, то это значение
            // многочлена
            case 1: {
                return result;
            }
            case 2: {
                String parts[] = (Double.toString(result)).split("\\.");
                boolean a1 = false;
                int countera1 = 0;
                boolean a2 = false;
                int countera2 = 0;
                boolean b1 = false;
                int counterb1 = 0;
                boolean b2 = false;
                int counterb2 = 0;
                for (int i = 0; i < parts[0].length(); ++i) {
                    int xx = parts[0].charAt(i) - '0';

                    if (xx  % 2 == 0) {
                        countera1++;
                    }
                }

                if (countera1 == parts[0].length()) a1 = true;

                for (int i = 0; i < parts[1].length(); ++i) {
                    int xx = parts[1].charAt(i) - '0';

                    if (xx % 2 == 0) {
                        countera2++;
                    }
                }

                if (countera2 == parts[1].length()) a2 = true;

                for (int i = 0; i < parts[0].length(); ++i) {
                    int xx = parts[0].charAt(i) - '0';

                    if (xx  % 2 != 0) {
                        counterb1++;
                    }
                }

                if (counterb1 == parts[0].length()) b1 = true;

                for (int i = 0; i < parts[1].length(); ++i) {
                    int xx = parts[1].charAt(i) - '0';

                    if (xx % 2 != 0) {
                        counterb2++;
                    }
                }

                if (counterb2 == parts[1].length()) b2 = true;

                if ((a1 && b2) || (a2 && b1)){
                    return true;
                }
                else return false;

            }
            default: return 0.0;
        }
    }

    public String getColumnName(int col) {
        switch (col) {
            case 0:
// Название 1-го столбца
                return "Значение X";
            case 1:
// Название 2-го столбца
                return "Значение многочлена";
            case 2:
                return "Разностороннее";
            default:
                return "";
        }
    }

    public Class<?> getColumnClass(int col) {
// И в 1-ом и во 2-ом столбце находятся значения типа Double
        if(col==2) return Boolean.class;
        else return Double.class;
    }
}
