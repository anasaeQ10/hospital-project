package com.mycompany.cli;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class HomePage extends JFrame {
    private JTable table;

    public HomePage(String nom, String prenom, String role) {
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
            new JButton("Afficher Médecins"),
            new JButton("Afficher Infirmiers"),
            new JButton("Afficher Réceptionnistes"),
            new JButton("Ajouter Médecin"),
            new JButton("Ajouter Infirmier"),
            new JButton("Ajouter Réceptionniste"),
            new JButton("Modifier Médecin"),
            new JButton("Modifier Infirmier"),
            new JButton("Modifier Réceptionniste"),
            new JButton("Supprimer Médecin"),
            new JButton("Supprimer Infirmier"),
            new JButton("Supprimer Réceptionniste"),
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
       buttons[0].addActionListener(e -> afficherMedecins());
        buttons[1].addActionListener(e -> afficherInfirmiers());
        buttons[2].addActionListener(e -> afficherReceptionnistes());
        buttons[3].addActionListener(e -> new AjouterMedecinFrame().setVisible(true));
        buttons[4].addActionListener(e -> new AjouterInfirmierFrame().setVisible(true));
        buttons[5].addActionListener(e -> new AjouterReceptionnisteFrame().setVisible(true));
        buttons[6].addActionListener(e -> modifierMedecin());
        buttons[7].addActionListener(e -> modifierInfirmier());
        buttons[8].addActionListener(e -> modifierReceptionniste());
        buttons[9].addActionListener(e -> supprimerMedecin());
        buttons[10].addActionListener(e -> supprimerInfirmier());
        buttons[11].addActionListener(e -> supprimerReceptionniste());
        buttons[12].addActionListener(e -> deconnexion());
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
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des données", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        
    }

    private String[] concatenate(String[] array, String element) {
        String[] newArray = new String[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = element;
        return newArray;
    }

    // Afficher les médecins
    private void afficherMedecins() {
        String[] colonnes = {"id","nom", "prenom", "specialite", "poste", "Mot_de_passe"};
        String query = "SELECT id,nom, prenom, specialite, poste, Mot_de_passe FROM medecins";
        remplirTableau(query, colonnes);
    }

    // Afficher les infirmiers
    private void afficherInfirmiers() {
        String[] colonnes = {"id","nom", "prenom", "specialite", "poste", "Mot_de_passe"};
        String query = "SELECT id,nom, prenom, specialite, poste, Mot_de_passe FROM infirmiers";
        remplirTableau(query, colonnes);
    }

    // Afficher les réceptionnistes
    private void afficherReceptionnistes() {
        String[] colonnes = {"id","nom", "prenom", "poste", "Mot_de_passe"};
        String query = "SELECT id,nom, prenom, poste, Mot_de_passe FROM Receptionniste";
        remplirTableau(query, colonnes);
    }

    // Ajouter médecin
    private void ajouterMedecin() {
        String nom = JOptionPane.showInputDialog(this, "Nom du Médecin:");
        String prenom = JOptionPane.showInputDialog(this, "Prénom du Médecin:");
        String specialite = JOptionPane.showInputDialog(this, "Spécialité:");
        String poste = JOptionPane.showInputDialog(this, "Poste:");
        String mot_de_passe = JOptionPane.showInputDialog("Mot_de_passe");

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            String sql = "INSERT INTO medecins (nom, prenom, specialite, poste, mot_de_passe) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, nom);
            statement.setString(2, prenom);
            statement.setString(3, specialite);
            statement.setString(4, poste);
            statement.setString(5, mot_de_passe);

            statement.executeUpdate();
            statement.close();
            conn.close();

            JOptionPane.showMessageDialog(this, "Médecin ajouté avec succès!");
        } catch (Exception ex) {
            ex.printStackTrace();
           JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Ajouter un infirmier
    private void ajouterInfirmier() {
        String nom = JOptionPane.showInputDialog(this, "Nom de l'Infirmier:");
        String prenom = JOptionPane.showInputDialog(this, "Prénom de l'Infirmier:");
        String specialite = JOptionPane.showInputDialog(this, "Spécialité:");
        String poste = JOptionPane.showInputDialog(this, "Poste:");
        String Mot_de_passe = JOptionPane.showInputDialog(this, "Mot_de_passe");

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            String sql = "INSERT INTO infirmiers (nom, prenom, specialite, poste,mot_de_passe) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, nom);
            statement.setString(2, prenom);
            statement.setString(3, specialite);
            statement.setString(4, poste);
            statement.setString(5, Mot_de_passe);

            statement.executeUpdate();
            statement.close();
            conn.close();

            JOptionPane.showMessageDialog(this, "Infirmier ajouté avec succès!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'infirmier", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Ajouter un réceptionniste
    private void ajouterReceptionniste() {
        String nom = JOptionPane.showInputDialog(this, "Nom du Réceptionniste:");
        String prenom = JOptionPane.showInputDialog(this, "Prénom du Réceptionniste:");
        String poste = JOptionPane.showInputDialog(this, "Poste:");
        String mot_de_passe = JOptionPane.showInputDialog("Mot_de_passe:");

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            String sql = "INSERT INTO Receptionniste (nom, prenom, poste, mot_de_passe) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, nom);
            statement.setString(2, prenom);
            statement.setString(3, poste);
            statement.setString(4, mot_de_passe);

            statement.executeUpdate();
            statement.close();
            conn.close();

            JOptionPane.showMessageDialog(this, "Réceptionniste ajouté avec succès!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du réceptionniste"+ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


     // Modifier un médecin
    private void modifierMedecin() {
       // Demander à l'utilisateur l'ID du médecin à modifier
    String idString = JOptionPane.showInputDialog(this, "Entrez l'ID du Médecin à modifier:");
    if (idString != null && !idString.isEmpty()) {
        int id = Integer.parseInt(idString);

        // Ouvrir la fenêtre de modification du médecin
        new modifierMedecinFrame(id).setVisible(true);
    }
    }

    // Modifier un infirmier
    private void modifierInfirmier() {
        // Demander à l'utilisateur l'ID de l'infirmière à modifier
    String idString = JOptionPane.showInputDialog(this, "Entrez l'ID de l'Infirmière à modifier:");
    if (idString != null && !idString.isEmpty()) {
        int id = Integer.parseInt(idString);
        
        // Créer et afficher la fenêtre de modification de l'infirmière avec l'ID passé en paramètre
        new modifierInfirmierFrame(id).setVisible(true);
    }
    }

    // Modifier un réceptionniste
    private void modifierReceptionniste() {
        // Demander à l'utilisateur l'ID de l'infirmière à modifier
    String idString = JOptionPane.showInputDialog(this, "Entrez l'ID du receptionniste à modifier:");
    if (idString != null && !idString.isEmpty()) {
        int id = Integer.parseInt(idString);
        
        // Créer et afficher la fenêtre de modification de l'infirmière avec l'ID passé en paramètre
        new ModifierReceptionnisteFrame(id).setVisible(true);
    }
    }

    // Supprimer un médecin
    private void supprimerMedecin() {
        String id = JOptionPane.showInputDialog(this, "ID du Médecin à supprimer:");

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            String sql = "DELETE FROM medecins WHERE id=?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(id));

            statement.executeUpdate();
            statement.close();
            conn.close();

            JOptionPane.showMessageDialog(this, "Médecin supprimé avec succès!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du médecin", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Supprimer un infirmier
    private void supprimerInfirmier() {
        String id = JOptionPane.showInputDialog(this, "ID de l'Infirmier à supprimer:");

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            String sql = "DELETE FROM infirmiers WHERE id=?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(id));

            statement.executeUpdate();
            statement.close();
            conn.close();

            JOptionPane.showMessageDialog(this, "Infirmier supprimé avec succès!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'infirmier", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Supprimer un réceptionniste
    private void supprimerReceptionniste() {
        String id = JOptionPane.showInputDialog(this, "ID du Réceptionniste à supprimer:");

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            String sql = "DELETE FROM Receptionniste WHERE id=?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(id));

            statement.executeUpdate();
            statement.close();
            conn.close();

            JOptionPane.showMessageDialog(this, "Réceptionniste supprimé avec succès!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du réceptionniste", "Erreur", JOptionPane.ERROR_MESSAGE);
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
