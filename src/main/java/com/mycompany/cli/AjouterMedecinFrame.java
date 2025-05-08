/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cli;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

class AjouterMedecinFrame extends JFrame {
    public AjouterMedecinFrame() {
        setTitle("Ajouter un Médecin");
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(7, 3, 12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel labelNom = new JLabel("Nom:");
        JTextField fieldNom = new JTextField();
        JLabel labelPrenom = new JLabel("Prénom:");
        JTextField fieldPrenom = new JTextField();
        JLabel labelSpecialite = new JLabel("Spécialité:");
        JTextField fieldSpecialite = new JTextField();
        JLabel labelDate = new JLabel("Date de naissance");
        JTextField fieldDate = new JTextField("YYYY-MM-DD", 20);
        JLabel labelPoste = new JLabel("Poste:");
        JTextField fieldPoste = new JTextField();
        JLabel labelMotDePasse = new JLabel("Mot de passe:");
        JPasswordField fieldMotDePasse = new JPasswordField();

        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.addActionListener(e -> {
            String nom = fieldNom.getText();
            String prenom = fieldPrenom.getText();
            String specialite = fieldSpecialite.getText();
            String date = fieldDate.getText();
            String poste = fieldPoste.getText();
            String motDePasse = new String(fieldMotDePasse.getPassword());
            String dateDebutService = new java.sql.Date(System.currentTimeMillis()).toString();

            try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");
                String sql = "INSERT INTO medecins (nom, prenom, specialite, date_naissance ,poste, mot_de_passe, date_debut_service) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, nom);
                statement.setString(2, prenom);
                statement.setString(3, specialite);
                statement.setString(4, date);
                statement.setString(5, poste);
                statement.setString(6, motDePasse);
                statement.setString(7, dateDebutService);

                statement.executeUpdate();
                statement.close();
                conn.close();

                JOptionPane.showMessageDialog(this, "Médecin ajouté avec succès!");
                this.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(labelNom);
        panel.add(fieldNom);
        panel.add(labelPrenom);
        panel.add(fieldPrenom);
        panel.add(labelSpecialite);
        panel.add(fieldSpecialite);
        panel.add(labelDate);
        panel.add(fieldDate);
        panel.add(labelPoste);
        panel.add(fieldPoste);
        panel.add(labelMotDePasse);
        panel.add(fieldMotDePasse);
        panel.add(new JLabel());
        panel.add(btnAjouter);

        add(panel);
    }
}
