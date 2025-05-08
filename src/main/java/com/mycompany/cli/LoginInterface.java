package com.mycompany.cli;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginInterface extends JFrame {
    
    public LoginInterface(){
        // Appliquer un Look and Feel moderne
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Créer le cadre principal
        JFrame frame = new JFrame("Connexion utilisateur");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 500);
        frame.setLayout(null);
        frame.setResizable(false);
        
        // Centrer la fenêtre sur l'écran
        frame.setLocationRelativeTo(null);

        // Couleurs principales
        Color backgroundColor = new Color(30, 58, 104);
        Color accentColor = new Color(255, 196, 37);
        Color textColor = Color.WHITE;

        frame.getContentPane().setBackground(backgroundColor);

        // Titre principal
        JLabel labelTitle = new JLabel("Connexion", SwingConstants.CENTER);
        labelTitle.setFont(new Font("Arial", Font.BOLD, 28));
        labelTitle.setForeground(textColor);
        labelTitle.setBounds(125, 20, 200, 40);

        // Sous-titre
        JLabel labelSubtitle = new JLabel("Mon compte", SwingConstants.CENTER);
        labelSubtitle.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 24));
        labelSubtitle.setForeground(accentColor);
        labelSubtitle.setBounds(125, 70, 200, 30);

        // Champ de texte pour le nom d'utilisateur
        JLabel labelUsername = new JLabel("Entrez votre nom");
        labelUsername.setForeground(textColor);
        labelUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        labelUsername.setBounds(50, 130, 350, 25);

        JTextField textFieldUsername = new JTextField();
        textFieldUsername.setBounds(50, 160, 350, 40);
        textFieldUsername.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        textFieldUsername.setBackground(new Color(255, 255, 255));
        textFieldUsername.setFont(new Font("Arial", Font.PLAIN, 16));

        // Champ de texte pour le mot de passe
        JLabel labelPassword = new JLabel("Entrez votre mot de passe");
        labelPassword.setForeground(textColor);
        labelPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        labelPassword.setBounds(50, 220, 350, 25);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(50, 250, 350, 40);
        passwordField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        passwordField.setBackground(new Color(255, 255, 255));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));

        // Bouton "Se connecter"
        JButton buttonLogin = new JButton("Se connecter");
        buttonLogin.setBounds(150, 320, 150, 40);
        buttonLogin.setBackground(accentColor);
        buttonLogin.setForeground(Color.BLACK);
        buttonLogin.setFocusPainted(false);
        buttonLogin.setFont(new Font("Arial", Font.BOLD, 16));
        buttonLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Ajouter une action au bouton Login
        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = textFieldUsername.getText();
                String password = new String(passwordField.getPassword());

                // Validation des identifiants
                String[] userInfo = validateLogin(username, password);
                if (userInfo != null) {
                    JOptionPane.showMessageDialog(frame, "Connexion réussie !");

                    // Fermer la fenêtre actuelle
                    frame.dispose();

                    // Rediriger vers la page d'accueil appropriée
                    redirectToHomePage(userInfo[0], userInfo[1], userInfo[2]);
                } else {
                    JOptionPane.showMessageDialog(frame, "Nom d'utilisateur ou mot de passe invalide .", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ajouter les composants au cadre
        frame.add(labelTitle);
        frame.add(labelSubtitle);
        frame.add(labelUsername);
        frame.add(textFieldUsername);
        frame.add(labelPassword);
        frame.add(passwordField);
        frame.add(buttonLogin);

        // Afficher le cadre
        frame.setVisible(true);
    }
    
    
    
    
    public static void main(String[] args) {
        new LoginInterface();
        
        
        
    }

    private static String[] validateLogin(String username, String password) {
        String[] userInfo = null;
        try {
            // Connexion à la base de données
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cli", "root", "root");

            // Préparer et exécuter la requête
            String sql = "SELECT id, nom, prenom, role FROM ("
    + " SELECT id, nom, prenom, mot_de_passe, role FROM administrateurs "
    + " UNION ALL "
    + " SELECT id, nom, prenom, mot_de_passe, role FROM medecins "
    + " UNION ALL "
    + " SELECT id, nom, prenom, mot_de_passe, role FROM infirmiers "
    + " UNION ALL "
    + " SELECT id, nom, prenom, mot_de_passe, role FROM Receptionniste "
    + ") AS users WHERE nom = ? AND mot_de_passe = ?";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Récupérer les informations de l'utilisateur
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String role = resultSet.getString("role");
                userInfo = new String[]{nom, prenom, role};
            }

            // Fermer les ressources
            resultSet.close();
            statement.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userInfo;
    }

    // Méthode pour rediriger vers la page d'accueil appropriée
    private static void redirectToHomePage(String nom, String prenom, String role) {
        switch (role.toLowerCase()) {
            case "admin":
                HomePage homePage = new HomePage(nom, prenom, role);
                homePage.setVisible(true);
                break;
            case "medecin":
                HomePageMedecin medecinPage = new HomePageMedecin(nom, prenom);
                medecinPage.setVisible(true);
                break;
            case "infirmiere":
                HomePageInfirmiere infirmierePage = new HomePageInfirmiere(nom, prenom);
                infirmierePage.setVisible(true);
                break;
            case "receptionniste":
                HomePageReceptionniste receptionnistePage = new HomePageReceptionniste(nom, prenom,role);
                receptionnistePage.setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Rôle non reconnu.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
