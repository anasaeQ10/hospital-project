package com.mycompany.cli;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class modifierPatientFrame extends JFrame {
    private JTextField tfID, tfNom, tfPrenom, tfDateNaissance, tfDateCreation, tfMaladies, tfTelephone, tfEmail;
    private JButton btnModifier;
    private int patientID;

    public modifierPatientFrame(int patientID) {
        setTitle("Modifier Patient");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        this.patientID = patientID;

        // Créer un panneau pour les champs d'entrée
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Champs pour les informations du patient
        inputPanel.add(new JLabel("ID :"), gbc);
        gbc.gridx++;
        tfID = new JTextField(String.valueOf(patientID));
        tfID.setEditable(false);
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
        inputPanel.add(new JLabel("Date de Naissance :"), gbc);
        gbc.gridx++;
        tfDateNaissance = new JTextField("YYYY-MM-DD");
        inputPanel.add(tfDateNaissance, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Date de Création :"), gbc);
        gbc.gridx++;
        tfDateCreation = new JTextField("YYYY-MM-DD");
        inputPanel.add(tfDateCreation, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Maladies :"), gbc);
        gbc.gridx++;
        tfMaladies = new JTextField();
        inputPanel.add(tfMaladies, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Téléphone :"), gbc);
        gbc.gridx++;
        tfTelephone = new JTextField();
        inputPanel.add(tfTelephone, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Email :"), gbc);
        gbc.gridx++;
        tfEmail = new JTextField();
        inputPanel.add(tfEmail, gbc);

        // Bouton de modification
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnModifier = new JButton("Modifier");
        btnModifier.setBackground(new Color(50, 150, 250));
        btnModifier.setForeground(Color.WHITE);
        btnModifier.setFocusPainted(false);
        btnModifier.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(btnModifier);

        // Charger les données du patient
        chargerDonneesPatient();

        // Action lors du clic sur le bouton Modifier
        btnModifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifierPatient();
            }
        });

        // Ajouter les panneaux à la fenêtre
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Charger les informations du patient depuis la base de données
    private void chargerDonneesPatient() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            String query = "SELECT * FROM patients WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, patientID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tfNom.setText(rs.getString("nom"));
                tfPrenom.setText(rs.getString("prenom"));
                tfDateNaissance.setText(rs.getString("date_naissance"));
                tfDateCreation.setText(rs.getString("date_creation_dossier"));
                tfMaladies.setText(rs.getString("maladies"));
                tfTelephone.setText(rs.getString("telephone"));
                tfEmail.setText(rs.getString("email"));
            } else {
                JOptionPane.showMessageDialog(this, "Patient non trouvé !");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données.");
        }
    }

    // Modifier le patient dans la base de données
    private void modifierPatient() {
        String nom = tfNom.getText();
        String prenom = tfPrenom.getText();
        String dateNaissance = tfDateNaissance.getText();
        String dateCreation = tfDateCreation.getText();
        String maladies = tfMaladies.getText();
        String telephone = tfTelephone.getText();
        String email = tfEmail.getText();

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            String query = "UPDATE patients SET nom = ?, prenom = ?, date_naissance = ?, date_creation_dossier = ?, maladies = ?, telephone = ?, email = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, dateNaissance);
            stmt.setString(4, dateCreation);
            stmt.setString(5, maladies);
            stmt.setString(6, telephone);
            stmt.setString(7, email);
            stmt.setInt(8, patientID);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Patient modifié avec succès.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification du patient.");
            }

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String idString = JOptionPane.showInputDialog("Entrez l'ID du patient à modifier :");
        if (idString != null && !idString.isEmpty()) {
            try {
                int id = Integer.parseInt(idString);
                SwingUtilities.invokeLater(() -> new modifierPatientFrame(id).setVisible(true));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "L'ID doit être un nombre entier.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez entrer un ID valide.");
        }
    }
}
