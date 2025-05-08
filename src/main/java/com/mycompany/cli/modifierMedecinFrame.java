package com.mycompany.cli;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class modifierMedecinFrame extends JFrame {
    private JTextField tfID, tfNom, tfPrenom, tfSpecialite, tfPoste, tfMotDePasse;
    private JButton btnModifier;
    private int idMedecin; // Stocker l'ID du médecin pour la modification

    public modifierMedecinFrame(int idMedecin) {
        setTitle("Modifier Médecin");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Espacement entre les composants

        this.idMedecin = idMedecin; // Initialiser l'ID du médecin

        // Créer un panneau pour les champs d'entrée
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Marges
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espacement entre les composants
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Champs de saisie pour les informations du médecin
        inputPanel.add(new JLabel("ID :"), gbc);
        gbc.gridx++;
        tfID = new JTextField(String.valueOf(idMedecin));
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

        // Charger les données du médecin
        chargerDonneesMedecin();

        // Action lors du clic sur le bouton Modifier
        btnModifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifierMedecin();
            }
        });

        // Ajouter les panneaux à la fenêtre
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Charger les informations du médecin depuis la base de données
    private void chargerDonneesMedecin() {
        try {
            // Connexion à la base de données
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            // Requête pour récupérer les informations du médecin
            String query = "SELECT * FROM medecins WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idMedecin); // Utilisation de l'ID du médecin
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Remplir les champs avec les données du médecin
                tfNom.setText(rs.getString("nom"));
                tfPrenom.setText(rs.getString("prenom"));
                tfSpecialite.setText(rs.getString("specialite"));
                tfPoste.setText(rs.getString("poste"));
                tfMotDePasse.setText(rs.getString("mot_de_passe"));
            } else {
                JOptionPane.showMessageDialog(this, "Médecin non trouvé !");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données.");
        }
    }

    // Fonction pour modifier les informations du médecin dans la base de données
    private void modifierMedecin() {
        String nom = tfNom.getText();
        String prenom = tfPrenom.getText();
        String specialite = tfSpecialite.getText();
        String poste = tfPoste.getText();
        String motDePasse = tfMotDePasse.getText();

        try {
            // Connexion à la base de données
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            // Requête de mise à jour
            String query = "UPDATE medecins SET nom = ?, prenom = ?, specialite = ?, poste = ?, mot_de_passe = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nom);  // Nouveau nom
            stmt.setString(2, prenom);  // Nouveau prénom
            stmt.setString(3, specialite);  // Nouvelle spécialité
            stmt.setString(4, poste);  // Nouveau poste
            stmt.setString(5, motDePasse);  // Nouveau mot de passe
            stmt.setInt(6, idMedecin);  // Utilisation de l'ID pour la mise à jour

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Médecin modifié avec succès.");
                dispose(); // Fermer la fenêtre après modification
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification du médecin.");
            }

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données.");
        }
    }

    public static void main(String[] args) {
        // Afficher la fenêtre de modification (pour tester la fenêtre seule)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new modifierMedecinFrame(1).setVisible(true); // Exemple avec ID 1
            }
        });
    }
}
