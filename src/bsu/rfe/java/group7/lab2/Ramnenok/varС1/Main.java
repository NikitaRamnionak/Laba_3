package bsu.rfe.java.group7.lab2.Ramnenok.varС1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Main extends JFrame {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;

    private Double[] coefficients;

    // Объект диалогового окна для выбора файлов
// Компонент не создаѐтся изначально, т.к. может и не понадобиться
// пользователю если тот не собирается сохранять данные в файл
    private JFileChooser fileChooser = null;

    // Элементы меню вынесены в поля данных класса, так как ими необходимо
// манипулировать из разных мест
    private JMenuItem saveToTextMenuItem;
    private JMenuItem saveToGraphicsMenuItem;
    private JMenuItem searchValueMenuItem;
    private JMenuItem aboutProgram;

    // Поля ввода для считывания значений переменных
    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldStep;
    private Box hBoxResult;

    // Визуализатор ячеек таблицы
    private GornerTableCellRenderer renderer = new GornerTableCellRenderer();

    // Модель данных с результатами вычислений
    private GornerTableModel data;

    public Main(Double[] coefficients) {
        super("Табулирование многочлена на отрезке по схеме Горнера");
        this.coefficients = coefficients;
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        JMenu tableMenu = new JMenu("Таблица");
        menuBar.add(tableMenu);
        JMenu referenceMenu = new JMenu("Справка");
        menuBar.add(referenceMenu);

        Action saveToTextAction = new AbstractAction("Сохранить в текстовый файл") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(Main.this) == JFileChooser.APPROVE_OPTION)
                    saveToTextFile(fileChooser.getSelectedFile());
            }
        };
        saveToTextMenuItem = fileMenu.add(saveToTextAction);
        saveToTextMenuItem.setEnabled(false);

        Action saveToGraphicsAction = new AbstractAction("Сохранить данные для построения графика") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(Main.this) == JFileChooser.APPROVE_OPTION) ;
                saveToGraphicsFile(fileChooser.getSelectedFile());
            }
        };
        saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
        saveToGraphicsMenuItem.setEnabled(false);

        Action searchValueAction = new AbstractAction("Найти значение многочлена") {
            public void actionPerformed(ActionEvent event) {
                String value = JOptionPane.showInputDialog(Main.this, "Введите значение для поиска", "Поиск значения", JOptionPane.QUESTION_MESSAGE);
                renderer.setNeedle(value);
                getContentPane().repaint();
            }
        };
        searchValueMenuItem = tableMenu.add(searchValueAction);
        searchValueMenuItem.setEnabled(false);

        Action aboutProgramAction = new AbstractAction("О программе") {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Main.this, "Рамнёнок Никита 7 группа");
            }
        };
        aboutProgram = referenceMenu.add(aboutProgramAction);
        aboutProgram.setEnabled(true);

        JLabel labelForFrom = new JLabel("X изменяется на интервале от:");
        textFieldFrom = new JTextField("0.0", 10);
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());
        JLabel labelForTo = new JLabel("до:");
        textFieldTo = new JTextField("1.0", 10);
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
        JLabel labelForStep = new JLabel("с шагом:");
        textFieldStep = new JTextField("0.1", 10);
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());

        Box hboxRange = Box.createHorizontalBox();
        hboxRange.setBorder(BorderFactory.createBevelBorder(1));
        hboxRange.add(Box.createHorizontalGlue());
        hboxRange.add(labelForFrom);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldFrom);
        hboxRange.add(Box.createHorizontalStrut(20));
        hboxRange.add(labelForTo);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldTo);
        hboxRange.add(Box.createHorizontalStrut(20));
        hboxRange.add(labelForStep);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldStep);
        hboxRange.add(Box.createHorizontalGlue());


        hboxRange.setPreferredSize(new Dimension(new Double(hboxRange.getMaximumSize().getWidth()).intValue(),
                new Double(hboxRange.getMinimumSize().getHeight()).intValue() * 2));
        getContentPane().add(hboxRange, BorderLayout.SOUTH);
    }
    protected void saveToGraphicsFile(File selectedFile) {
        try {
// Создать новый байтовый поток вывода, направленный в указанный файл
            DataOutputStream out = new DataOutputStream(new
                    FileOutputStream(selectedFile));
// Записать в поток вывода попарно значение X в точке, значение многочлена в точке
            for (int i = 0; i < data.getRowCount(); i++) {
                out.writeDouble((Double) data.getValueAt(i, 0));
                out.writeDouble((Double) data.getValueAt(i, 1));
            }
// Закрыть поток вывода
            out.close();
        } catch (Exception e) {
// Исключительную ситуацию "ФайлНеНайден" в данном случае можно не обрабатывать,
// так как мы файл создаѐм, а не открываем для чтения
        }
    }
    protected void saveToTextFile(File selectedFile) {
        try {
// Создать новый символьный поток вывода, направленный в указанный файл
            PrintStream out = new PrintStream(selectedFile);
// Записать в поток вывода заголовочные сведения
            out.println("Результаты табулирования многочлена по схеме  Горнера");
            out.print("Многочлен: ");
            for (int i = 0; i < coefficients.length; i++) {
                out.print(coefficients[i] + "*X^" +
                        (coefficients.length - i - 1));
                if (i != coefficients.length - 1)
                    out.print(" + ");
            }
            out.println("");
            out.println("Интервал от " + data.getFrom() + " до " +
                    data.getTo() + " с шагом " + data.getStep());
            out.println("====================================================");
// Записать в поток вывода значения в точках
            for (int i = 0; i < data.getRowCount(); i++) {
                out.println("Значение в точке " + data.getValueAt(i, 0)
                        + " равно " + data.getValueAt(i, 1));
            }
// Закрыть поток
            out.close();
        } catch (FileNotFoundException e) {
// Исключительную ситуацию "ФайлНеНайден" можно не
// обрабатывать, так как мы файл создаѐм, а не открываем
        }
    }
    public static void main(String[] args) {
    }
}
