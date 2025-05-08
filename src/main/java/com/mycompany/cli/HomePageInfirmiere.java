package com.mycompany.cli;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class HomePageInfirmiere extends JFrame {
    private JTable tableReclamations;
    private String nom, prenom;

    public HomePageInfirmiere(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;

        // Paramètres de la fenêtre
        setTitle("Page d'Accueil Infirmière");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(243, 243, 243));

        // En-tête
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(34, 40, 49));
        headerPanel.setPreferredSize(new Dimension(1000, 100));

        JLabel labelTitle = new JLabel("Bienvenue " + nom + " " + prenom);
        labelTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        labelTitle.setForeground(Color.WHITE);
        labelTitle.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(labelTitle);

        // Panneau de navigation
        JPanel navPanel = new JPanel(new GridLayout(0, 1, 20, 20));
        navPanel.setBackground(new Color(56, 64, 78));
        navPanel.setPreferredSize(new Dimension(250, 0));

        JButton btnAfficherReclamations = new JButton("Afficher les Réclamations");
        btnAfficherReclamations.setBackground(new Color(30, 136, 229));
        btnAfficherReclamations.setForeground(Color.WHITE);
        btnAfficherReclamations.setFocusPainted(false);
        btnAfficherReclamations.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnAfficherReclamations.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnAfficherReclamations.addActionListener(e -> loadReclamations());
        navPanel.add(btnAfficherReclamations);

        JButton btnDeconnexion = new JButton("Se Déconnecter");
        btnDeconnexion.setBackground(new Color(30, 136, 229));
        btnDeconnexion.setForeground(Color.WHITE);
        btnDeconnexion.setFocusPainted(false);
        btnDeconnexion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnDeconnexion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnDeconnexion.addActionListener(e -> deconnexion());
        navPanel.add(btnDeconnexion);

        // Tableau des réclamations
        String[] columns = {"ID Réclamation", "Nom Patient", "Motif", "Date de Dépôt", "Traité", "Médecin"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Seule la colonne checkbox est éditable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 4 ? Boolean.class : String.class;
            }
        };

        tableReclamations = new JTable(tableModel);
        tableReclamations.setFont(new Font("Arial", Font.PLAIN, 14));
        tableReclamations.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tableReclamations);

        // Bouton pour enregistrer les modifications des réclamations
        JButton btnEnregistrer = new JButton("Enregistrer les Modifications");
        btnEnregistrer.setBackground(new Color(76, 175, 80));
        btnEnregistrer.setForeground(Color.WHITE);
        btnEnregistrer.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnEnregistrer.addActionListener(e -> updateReclamations());

        // Ajouter les composants au panneau principal
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(navPanel, BorderLayout.WEST);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(btnEnregistrer, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadReclamations() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            String sql = "SELECT r.id_reclamation, p.nom AS patient_nom, r.motif, r.date_depot, r.validation_medecin, "
                    + "m.nom AS medecin_nom "
                    + "FROM reclamations r "
                    + "JOIN patients p ON r.id_patient = p.id "
                    + "LEFT JOIN medecins m ON r.id_medecin = m.id "
                    + "WHERE r.id_infirmier = (SELECT id FROM infirmiers WHERE nom = ? AND prenom = ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nom);
            stmt.setString(2, prenom);

            ResultSet rs = stmt.executeQuery();
            DefaultTableModel tableModel = (DefaultTableModel) tableReclamations.getModel();
            tableModel.setRowCount(0);

            while (rs.next()) {
                int idReclamation = rs.getInt("id_reclamation");
                String patientNom = rs.getString("patient_nom");
                String motif = rs.getString("motif");
                Date dateDepot = rs.getDate("date_depot");
                boolean traite = rs.getInt("validation_medecin") == 1;
                String medecinNom = rs.getString("medecin_nom");

                tableModel.addRow(new Object[]{idReclamation, patientNom, motif, dateDepot, traite, medecinNom});
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des réclamations.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateReclamations() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");
            DefaultTableModel tableModel = (DefaultTableModel) tableReclamations.getModel();

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int idReclamation = (int) tableModel.getValueAt(i, 0);
                boolean traite = (boolean) tableModel.getValueAt(i, 4);

                String sql = "UPDATE reclamations SET validation_medecin = ? WHERE id_reclamation = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, traite ? 1 : 0);
                stmt.setInt(2, idReclamation);
                stmt.executeUpdate();
                stmt.close();
            }

            conn.close();
            JOptionPane.showMessageDialog(this, "Modifications enregistrées avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour des réclamations.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deconnexion() {
        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir vous déconnecter?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            // Créer une nouvelle instance de la page de connexion
            //LoginInterface loginInterface =
                    new LoginInterface();
            
            // Rendre visible la page de connexion
            //loginInterface.setVisible(true);
            // Fermer la page actuelle (la page de la page d'accueil par exemple)
            this.dispose();  // Ferme la fenêtre actuelle (accueil, etc.)
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomePageInfirmiere("NomInfirmiere", "PrenomInfirmiere").setVisible(true));
    }
}
