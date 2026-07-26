import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class CGPACalculator extends JFrame {
    private JTextField subjectField, creditsField, gradePointField;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel cgpaValueLabel, statusLabel;

    // Color Palette (Executive Dashboard Theme)
    private final Color COLOR_PRIMARY = new Color(37, 99, 235);     // Royal Blue
    private final Color COLOR_SUCCESS = new Color(16, 185, 129);   // Emerald Green
    private final Color COLOR_ACCENT = new Color(15, 23, 42);      // Slate 900
    private final Color COLOR_BG = new Color(241, 245, 249);       // Light Slate Background
    private final Color COLOR_CARD = Color.WHITE;
    private final Color COLOR_TEXT_MUTED = new Color(100, 116, 139);

    public CGPACalculator() {
        // Enforce consistent button background color rendering across all Operating Systems
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        setTitle("Academic Performance Tracker");
        setSize(720, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG);
        setLayout(new BorderLayout(15, 15));

        // 1. Header Banner
        add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Center Content (Input Form + Data Table)
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        centerPanel.add(createInputCard(), BorderLayout.NORTH);
        centerPanel.add(createTableCard(), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // 3. Bottom Summary Badge
        add(createSummaryPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_ACCENT);
        header.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("Student CGPA Calculator");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Track course credits and calculate overall grade point average");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(203, 213, 225));

        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);
        return header;
    }

    private JPanel createInputCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(COLOR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(15, 20, 15, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form Field Labels
        JLabel lblSubject = new JLabel("Subject Name");
        lblSubject.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSubject.setForeground(COLOR_ACCENT);
        subjectField = createStyledTextField();

        JLabel lblCredits = new JLabel("Credits");
        lblCredits.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCredits.setForeground(COLOR_ACCENT);
        creditsField = createStyledTextField();

        JLabel lblGrade = new JLabel("Grade Point (0-10)");
        lblGrade.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblGrade.setForeground(COLOR_ACCENT);
        gradePointField = createStyledTextField();

        // Row 0: Labels
        gbc.gridy = 0; gbc.gridx = 0; gbc.weightx = 0.5; card.add(lblSubject, gbc);
        gbc.gridx = 1; gbc.weightx = 0.25; card.add(lblCredits, gbc);
        gbc.gridx = 2; gbc.weightx = 0.25; card.add(lblGrade, gbc);

        // Row 1: Text Inputs
        gbc.gridy = 1; gbc.gridx = 0; card.add(subjectField, gbc);
        gbc.gridx = 1; card.add(creditsField, gbc);
        gbc.gridx = 2; card.add(gradePointField, gbc);

        // Row 2: Action Buttons (High Visibility)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setOpaque(false);

        JButton btnAdd = createHighVisibilityButton("Add Course", COLOR_PRIMARY);
        JButton btnCalculate = createHighVisibilityButton("Calculate CGPA", COLOR_SUCCESS);

        btnAdd.addActionListener(e -> addSubject());
        btnCalculate.addActionListener(e -> calculateCGPA());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnCalculate);

        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 3;
        gbc.insets = new Insets(18, 10, 5, 10);
        card.add(buttonPanel, gbc);

        return card;
    }

    private JPanel createTableCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(COLOR_CARD);
        card.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

        String[] columns = {"Subject Name", "Credits", "Grade Points"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(36);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(239, 246, 255));
        table.setSelectionForeground(COLOR_ACCENT);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Table Header Styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(COLOR_ACCENT);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 38));

        // Center Align Numeric Columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(COLOR_CARD);

        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    private JPanel createSummaryPanel() {
        JPanel summaryCard = new JPanel(new BorderLayout());
        summaryCard.setBackground(COLOR_CARD);
        summaryCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)),
                new EmptyBorder(15, 25, 20, 25)
        ));

        JLabel title = new JLabel("OVERALL CGPA");
        title.setFont(new Font("Segoe UI", Font.BOLD, 11));
        title.setForeground(COLOR_TEXT_MUTED);

        cgpaValueLabel = new JLabel("0.00");
        cgpaValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        cgpaValueLabel.setForeground(COLOR_PRIMARY);

        statusLabel = new JLabel("Add courses to calculate your GPA");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        statusLabel.setForeground(COLOR_TEXT_MUTED);

        JPanel leftGroup = new JPanel(new GridLayout(2, 1));
        leftGroup.setOpaque(false);
        leftGroup.add(title);
        leftGroup.add(cgpaValueLabel);

        summaryCard.add(leftGroup, BorderLayout.WEST);
        summaryCard.add(statusLabel, BorderLayout.EAST);

        return summaryCard;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(100, 34));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        return field;
    }

    private JButton createHighVisibilityButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bg);
        
        // Ensure background colors paint properly across Windows/Mac/Linux
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        // Fixed sizing to prevent text clipping
        button.setPreferredSize(new Dimension(150, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void addSubject() {
        String subject = subjectField.getText().trim();
        String creditsStr = creditsField.getText().trim();
        String gradeStr = gradePointField.getText().trim();

        if (subject.isEmpty() || creditsStr.isEmpty() || gradeStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please complete all input fields.", "Missing Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double credits = Double.parseDouble(creditsStr);
            double grade = Double.parseDouble(gradeStr);

            if (grade < 0 || grade > 10) {
                JOptionPane.showMessageDialog(this, "Grade points must be between 0 and 10.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            tableModel.addRow(new Object[]{subject, String.format("%.1f", credits), String.format("%.1f", grade)});
            subjectField.setText("");
            creditsField.setText("");
            gradePointField.setText("");
            subjectField.requestFocus();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credits and Grade Points must be valid numeric values.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateCGPA() {
        int rowCount = tableModel.getRowCount();
        if (rowCount == 0) {
            JOptionPane.showMessageDialog(this, "No course data available to calculate.", "Empty Table", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        double totalCredits = 0;
        double totalWeightedPoints = 0;

        for (int i = 0; i < rowCount; i++) {
            double credits = Double.parseDouble(tableModel.getValueAt(i, 1).toString());
            double gradePoints = Double.parseDouble(tableModel.getValueAt(i, 2).toString());

            totalCredits += credits;
            totalWeightedPoints += (credits * gradePoints);
        }

        if (totalCredits > 0) {
            double cgpa = totalWeightedPoints / totalCredits;
            cgpaValueLabel.setText(String.format("%.2f", cgpa));

            if (cgpa >= 8.0) {
                statusLabel.setText("Status: Distinction / First Class");
                cgpaValueLabel.setForeground(COLOR_SUCCESS);
            } else if (cgpa >= 6.0) {
                statusLabel.setText("Status: Second Class");
                cgpaValueLabel.setForeground(COLOR_PRIMARY);
            } else {
                statusLabel.setText("Status: Average");
                cgpaValueLabel.setForeground(new Color(217, 119, 6)); // Amber
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CGPACalculator().setVisible(true));
    }
}