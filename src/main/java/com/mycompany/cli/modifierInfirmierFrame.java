package com.mycompany.cli;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class modifierInfirmierFrame extends JFrame {
    private JTextField tfID, tfNom, tfPrenom, tfSpecialite, tfPoste, tfMotDePasse;
    private JButton btnModifier;
    private int idInfirmier; // Stocker l'ID de l'infirmière pour la modification

    public modifierInfirmierFrame(int idInfirmier) {
        setTitle("Modifier Infirmière");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Ajout d'espace entre les composants

        this.idInfirmier = idInfirmier; // Initialiser l'ID de l'infirmière

        // Créer un panneau pour les champs d'entrée
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Marges
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espacement entre les composants
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Champs de saisie pour les informations de l'infirmière
        inputPanel.add(new JLabel("ID :"), gbc);
        gbc.gridx++;
        tfID = new JTextField(String.valueOf(idInfirmier));
        tfID.setEditable(false); // L'ID ne doit pas être modifiable
        inputPanel.add(tfID, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Nom :"), gbc);
        gbc.gridx++;
        tfNom = new JTextField();
        inputPanel.add(tfNom, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Prénom :"), gbc);
        gbc.gridx++;
        tfPrenom = new JTextField();
        inputPanel.add(tfPrenom, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Spécialité :"), gbc);
        gbc.gridx++;
        tfSpecialite = new JTextField();
        inputPanel.add(tfSpecialite, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Poste :"), gbc);
        gbc.gridx++;
        tfPoste = new JTextField();
        inputPanel.add(tfPoste, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Mot de passe :"), gbc);
        gbc.gridx++;
        tfMotDePasse = new JTextField();
        inputPanel.add(tfMotDePasse, gbc);

        // Bouton de modification
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnModifier = new JButton("Modifier");
        btnModifier.setBackground(new Color(50, 150, 250));
        btnModifier.setForeground(Color.WHITE);
        btnModifier.setFocusPainted(false);
        btnModifier.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(btnModifier);

        // Charger les données de l'infirmière
        chargerDonneesInfirmiere();

        // Action lors du clic sur le bouton Modifier
        btnModifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifierInfirmiere();
            }
        });

        // Ajouter les panneaux à la fenêtre
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Charger les informations de l'infirmière depuis la base de données
    private void chargerDonneesInfirmiere() {
        try {
            // Connexion à la base de données
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            // Requête pour récupérer les informations de l'infirmière
            String query = "SELECT * FROM infirmiers WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idInfirmier);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Remplir les champs avec les données de l'infirmière
                tfNom.setText(rs.getString("nom"));
                tfPrenom.setText(rs.getString("prenom"));
                tfSpecialite.setText(rs.getString("specialite"));
                tfPoste.setText(rs.getString("poste"));
                tfMotDePasse.setText(rs.getString("mot_de_passe"));
            } else {
                JOptionPane.showMessageDialog(this, "Infirmière non trouvée !");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données.");
        }
    }

    // Fonction pour modifier les informations de l'infirmière dans la base de données
    private void modifierInfirmiere() {
        String nom = tfNom.getText();
        String prenom = tfPrenom.getText();
        String specialite = tfSpecialite.getText();
        String poste = tfPoste.getText();
        String motDePasse = tfMotDePasse.getText();

        try {
            // Connexion à la base de données
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            // Requête de mise à jour
            String query = "UPDATE infirmiers SET nom = ?, prenom = ?, specialite = ?, poste = ?, mot_de_passe = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, specialite);
            stmt.setString(4, poste);
            stmt.setString(5, motDePasse);
            stmt.setInt(6, idInfirmier);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Infirmière modifiée avec succès.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification de l'infirmière.");
            }

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String idString = JOptionPane.showInputDialog("Entrez l'ID de l'infirmière à modifier :");
        if (idString != null && !idString.isEmpty()) {
            try {
                int id = Integer.parseInt(idString);
                SwingUtilities.invokeLater(() -> new modifierInfirmierFrame(id).setVisible(true));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "L'ID doit être un nombre entier.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez entrer un ID valide.");
        }
    }
}
