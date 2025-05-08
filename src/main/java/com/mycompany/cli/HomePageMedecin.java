package com.mycompany.cli;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class HomePageMedecin extends JFrame {
    private JTable tableRendezvous;
    private JTable tableReclamations;
    private String nom, prenom;
    private JPanel cardPanel; // Panneau principal avec CardLayout

    public HomePageMedecin(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;

        // Paramètres de la fenêtre
        setTitle("Page d'Accueil Médecin");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panneau principal avec un Layout simplifié
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(243, 243, 243));

        // En-tête avec une couleur d'arrière-plan et titre
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(34, 40, 49));
        headerPanel.setPreferredSize(new Dimension(1000, 100));

        JLabel labelTitle = new JLabel("Bienvenue Dr. " + nom + " " + prenom);
        labelTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        labelTitle.setForeground(Color.WHITE);
        labelTitle.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(labelTitle);

        // Panneau de navigation (menu) sur le côté gauche
        JPanel navPanel = new JPanel(new GridLayout(0, 1, 20, 20));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        navPanel.setBackground(new Color(56, 64, 78));
        navPanel.setPreferredSize(new Dimension(250, 0));

        // Bouton pour afficher les rendez-vous
        JButton btnAfficherRendezvous = new JButton("Afficher les Rendez-vous");
        btnAfficherRendezvous.setBackground(new Color(30, 136, 229));
        btnAfficherRendezvous.setForeground(Color.WHITE);
        btnAfficherRendezvous.setFocusPainted(false);
        btnAfficherRendezvous.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnAfficherRendezvous.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnAfficherRendezvous.addActionListener(e -> loadAppointments());
        navPanel.add(btnAfficherRendezvous);

        // Nouveau bouton pour afficher les réclamations
        JButton btnAfficherReclamations = new JButton("Afficher les Réclamations");
        btnAfficherReclamations.setBackground(new Color(30, 136, 229));
        btnAfficherReclamations.setForeground(Color.WHITE);
        btnAfficherReclamations.setFocusPainted(false);
        btnAfficherReclamations.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnAfficherReclamations.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnAfficherReclamations.addActionListener(e -> loadReclamations());
        navPanel.add(btnAfficherReclamations);

        // Bouton pour ajouter une réclamation
        JButton btnAjouterReclamation = new JButton("Ajouter une Réclamation");
        btnAjouterReclamation.setBackground(new Color(30, 136, 229));
        btnAjouterReclamation.setForeground(Color.WHITE);
        btnAjouterReclamation.setFocusPainted(false);
        btnAjouterReclamation.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnAjouterReclamation.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnAjouterReclamation.addActionListener(e -> ajouterReclamation());
        navPanel.add(btnAjouterReclamation);

        // Bouton de déconnexion
        JButton btnDeconnexion = new JButton("Se Déconnecter");
        btnDeconnexion.setBackground(new Color(30, 136, 229));
        btnDeconnexion.setForeground(Color.WHITE);
        btnDeconnexion.setFocusPainted(false);
        btnDeconnexion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnDeconnexion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnDeconnexion.addActionListener(e -> deconnexion());
        navPanel.add(btnDeconnexion);

        // Table des rendez-vous
        String[] columnsRendezvous = {"Nom Patient", "Prénom Patient", "Motif", "Date du Rendez-vous"};
        DefaultTableModel tableModelRendezvous = new DefaultTableModel(columnsRendezvous, 0);
        tableRendezvous = new JTable(tableModelRendezvous);
        tableRendezvous.setFont(new Font("Arial", Font.PLAIN, 14));
        tableRendezvous.setRowHeight(25);
        JScrollPane scrollPaneRendezvous = new JScrollPane(tableRendezvous);

        // Table des réclamations (avec une nouvelle colonne "Infirmier")
        String[] columnsReclamations = {"Nom Patient", "Motif de Réclamation", "Date de Dépôt", "État", "Infirmier"};
        DefaultTableModel tableModelReclamations = new DefaultTableModel(columnsReclamations, 0);
        tableReclamations = new JTable(tableModelReclamations);
        tableReclamations.setFont(new Font("Arial", Font.PLAIN, 14));
        tableReclamations.setRowHeight(25);
        JScrollPane scrollPaneReclamations = new JScrollPane(tableReclamations);

        // Panneau central avec CardLayout
        cardPanel = new JPanel(new CardLayout());
        cardPanel.add(scrollPaneRendezvous, "Rendezvous");
        cardPanel.add(scrollPaneReclamations, "Reclamations");

        // Ajouter les composants au panneau principal
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(navPanel, BorderLayout.WEST);
        mainPanel.add(cardPanel, BorderLayout.CENTER);

        // Ajouter le panneau principal à la fenêtre
        add(mainPanel);
    }

    private void loadAppointments() {
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, "Rendezvous");
        // Chargement des rendez-vous
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");
            String sql = "SELECT id FROM medecins WHERE nom = ? AND prenom = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int medecinId = rs.getInt("id");

                String query = "SELECT p.nom AS patient_nom, p.prenom AS patient_prenom, r.motif, r.date_rendez_vous "
                        + "FROM rendez_vous r "
                        + "JOIN patients p ON r.patient_id = p.id "
                        + "WHERE r.medecin_id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, medecinId);
                ResultSet resultSet = ps.executeQuery();

                DefaultTableModel tableModel = (DefaultTableModel) tableRendezvous.getModel();
                tableModel.setRowCount(0);

                while (resultSet.next()) {
                    String patientNom = resultSet.getString("patient_nom");
                    String patientPrenom = resultSet.getString("patient_prenom");
                    String motif = resultSet.getString("motif");
                    Date dateRendezvous = resultSet.getDate("date_rendez_vous");

                    tableModel.addRow(new Object[]{patientNom, patientPrenom, motif, dateRendezvous});
                }

                resultSet.close();
                ps.close();
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des rendez-vous.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadReclamations() {
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, "Reclamations");

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");
            String sql = "SELECT id FROM medecins WHERE nom = ? AND prenom = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int medecinId = rs.getInt("id");

                String query = "SELECT p.nom AS patient_nom, r.motif, r.date_depot, r.validation_medecin, i.nom AS infirmier_nom "
                        + "FROM reclamations r "
                        + "JOIN patients p ON r.id_patient = p.id "
                        + "LEFT JOIN infirmiers i ON r.id_infirmier = i.id "
                        + "WHERE r.id_medecin = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, medecinId);
                ResultSet resultSet = ps.executeQuery();

                DefaultTableModel tableModel = (DefaultTableModel) tableReclamations.getModel();
                tableModel.setRowCount(0);

                while (resultSet.next()) {
                    String patientNom = resultSet.getString("patient_nom");
                    String motif = resultSet.getString("motif");
                    Date dateDepot = resultSet.getDate("date_depot");
                    int validationMedecin = resultSet.getInt("validation_medecin");
                    String infirmierNom = resultSet.getString("infirmier_nom");

                    String etatReclamation = (validationMedecin == 1) ? "Traité" : "Non Traité";

                    tableModel.addRow(new Object[]{patientNom, motif, dateDepot, etatReclamation, infirmierNom});
                }

                resultSet.close();
                ps.close();
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des réclamations.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ajouterReclamation() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            // Sélectionner un infirmier aléatoire
            String getRandomInfirmierQuery = "SELECT id FROM infirmiers ORDER BY RAND() LIMIT 1";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getRandomInfirmierQuery);

            if (rs.next()) {
                int infirmierId = rs.getInt("id");

                // Récupérer les informations pour la réclamation
                String patientId = JOptionPane.showInputDialog(this, "Entrez l'ID du patient :");
                String motif = JOptionPane.showInputDialog(this, "Entrez le motif de la réclamation :");

                if (patientId != null && motif != null && !patientId.isEmpty() && !motif.isEmpty()) {
                    // Insérer la réclamation
                    String insertReclamationQuery = "INSERT INTO reclamations (id_patient, id_medecin, id_infirmier, motif, date_depot) VALUES (?, ?, ?, ?, CURRENT_DATE)";
                    PreparedStatement ps = conn.prepareStatement(insertReclamationQuery);
                    ps.setInt(1, Integer.parseInt(patientId));
                    ps.setString(2, getMedecinId());
                    ps.setInt(3, infirmierId);
                    ps.setString(4, motif);

                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Réclamation ajoutée avec succès !");
                    ps.close();
                } else {
                    JOptionPane.showMessageDialog(this, "Les champs ID patient et motif sont obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Aucun infirmier trouvé dans la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la réclamation.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getMedecinId() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");
        String sql = "SELECT id FROM medecins WHERE nom = ? AND prenom = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nom);
        stmt.setString(2, prenom);
        ResultSet rs = stmt.executeQuery();

        String medecinId = null;
        if (rs.next()) {
            medecinId = rs.getString("id");
        }

        rs.close();
        stmt.close();
        conn.close();

        return medecinId;
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
        // SwingUtilities.invokeLater(() -> new HomePageMedecin("NomMedecin", "PrenomMedecin").setVisible(true));
    }
}
