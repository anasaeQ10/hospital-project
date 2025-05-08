package com.mycompany.cli;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class HomePageReceptionniste extends JFrame {
    private JTable table;

    public HomePageReceptionniste(String nom, String prenom,String role) {
        setTitle("Page d'Accueil");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(243, 243, 243));

        // Panneau d'en-tête
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(34, 40, 49));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel welcomeLabel = new JLabel("Bienvenue, " + nom + " " + prenom + " !", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Panneau de navigation
        JPanel navPanel = new JPanel(new GridLayout(0, 1, 20, 20));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        navPanel.setBackground(new Color(56, 64, 78));
        navPanel.setPreferredSize(new Dimension(250, 0));

        JButton[] buttons = {
            new JButton("Afficher Patients"),
            new JButton("Afficher Rendez_vous"),
            
            new JButton("Ajouter Patients"),
            new JButton("Ajouter Rendez_vous"),
            
            new JButton("Modifier Patients"),
            new JButton("Modifier Rendez_vous"),
            
            new JButton("Supprimer Patients"),
            new JButton("Supprimer Rendez_vous"),
            
            new JButton("Se Déconnecter")
        };

        for (JButton button : buttons) {
            button.setBackground(new Color(30, 136, 229));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            navPanel.add(button);
        }

        // Tableau pour afficher les données
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Informations"));

        // Ajout des panneaux au panneau principal
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(navPanel, BorderLayout.WEST);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        // Événements pour les boutons
        buttons[0].addActionListener(e -> afficherPatients());
        buttons[1].addActionListener(e -> afficherRendez_vous());
        buttons[2].addActionListener(e -> ajouterPatients());
        buttons[3].addActionListener(e -> ajouterRendez_vous());
        buttons[4].addActionListener(e -> modifierPatients());
        buttons[5].addActionListener(e -> modifierRendez_vous());
        buttons[6].addActionListener(e -> supprimerPatients());
        buttons[7].addActionListener(e -> supprimerRendez_vous());
        buttons[8].addActionListener(e -> deconnexion());
    }

    // Méthode générique pour remplir le tableau
    private void remplirTableau(String query, String[] colonnes) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == colonnes.length; // Modifier uniquement la colonne d'actions
            }
        };
        model.setColumnIdentifiers(concatenate(colonnes, "Actions"));
        table.setModel(model);

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Object[] ligne = new Object[colonnes.length +1];
                for (int i = 0; i < colonnes.length; i++) {
                    ligne[i] = resultSet.getObject(colonnes[i]);
                }
                //ligne[colonnes.length] = "Modifier / Supprimer";
                model.addRow(ligne);
            }

            resultSet.close();
            statement.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des données"+ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        
    }

    private String[] concatenate(String[] array, String element) {
        String[] newArray = new String[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = element;
        return newArray;
    }


    // Afficher les médecins
    private void afficherPatients() {
    String[] colonnes = {"ID", "Nom", "Prenom", "date_naissance", "Telephone", "email"};
    String query = "SELECT id, nom, prenom, date_naissance, Telephone, email FROM patients";
    remplirTableau(query, colonnes);
}


    // Afficher les infirmiers
    private void afficherRendez_vous() {
        String[] colonnes = {"ID", "Nom", "Prenom", "Date_Rendez_vous", "Motif", "nom_medecin"};
        String query = """
            SELECT rv.id, p.nom, p.prenom, rv.date_rendez_vous, rv.motif, m.nom AS nom_medecin
            FROM rendez_vous rv
            JOIN patients p ON rv.patient_id = p.id
            JOIN medecins m ON rv.medecin_id = m.id
        """;
        remplirTableau(query, colonnes);
    }


    

    // Ajouter médecin
    public void ajouterPatients() {
    SwingUtilities.invokeLater(() -> {
        AjouterPatientFrame ajouterPatientFrame = new AjouterPatientFrame();
        ajouterPatientFrame.setVisible(true);
    });
}

    // Ajouter un infirmier
    private void ajouterRendez_vous() {
        SwingUtilities.invokeLater(() -> {
        AjouterRendezvousFrame ajouterPatientFrame = new AjouterRendezvousFrame();
        ajouterPatientFrame.setVisible(true);
    });
    }

    

     // Modifier un médecin
    private void modifierPatients() {
       // Demander à l'utilisateur l'ID du médecin à modifier
    String idString = JOptionPane.showInputDialog(this, "Entrez l'ID du Médecin à modifier:");
    if (idString != null && !idString.isEmpty()) {
        int id = Integer.parseInt(idString);

        // Ouvrir la fenêtre de modification du médecin
        new modifierPatientFrame(id).setVisible(true);
    }
    }

    // Modifier un infirmier
    private void modifierRendez_vous() {
        // Demander à l'utilisateur l'ID de l'infirmière à modifier
    String idString = JOptionPane.showInputDialog(this, "Entrez l'ID du rendez_vous à modifier:");
    if (idString != null && !idString.isEmpty()) {
        int id = Integer.parseInt(idString);
        
        // Créer et afficher la fenêtre de modification de l'infirmière avec l'ID passé en paramètre
        new modifierrendezvousFrame(id).setVisible(true);
    }
    }

    

    // Supprimer un médecin
    private void supprimerPatients() {
        String id = JOptionPane.showInputDialog(this, "ID du patient à supprimer:");

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            String sql = "DELETE FROM patients WHERE id=?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(id));

            statement.executeUpdate();
            statement.close();
            conn.close();

            JOptionPane.showMessageDialog(this, "Patient supprimé avec succès!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du médecin"+ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Supprimer un infirmier
    private void supprimerRendez_vous() {
        String id = JOptionPane.showInputDialog(this, "ID du Rendez_vous à supprimer:");

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            String sql = "DELETE FROM rendez_vous WHERE id=?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(id));

            statement.executeUpdate();
            statement.close();
            conn.close();

            JOptionPane.showMessageDialog(this, "Rendez_vous supprimé avec succès!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'infirmier", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    // Déconnexion
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
        SwingUtilities.invokeLater(() -> {
            //new HomePage("Anas", "AIT EL QADI", "").setVisible(true);
        });
    }
}
