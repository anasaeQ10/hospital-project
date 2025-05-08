
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

class ButtonRenderer extends JPanel implements TableCellRenderer {
    private final JButton btnModifier;
    private final JButton btnSupprimer;

    public ButtonRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");
        add(btnModifier);
        add(btnSupprimer);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}
