package com.jsuchinski.galeria.servlet;

import com.jsuchinski.galeria.db.DAO;
import com.jsuchinski.galeria.db.MariaDB_DAOImlp;
import com.jsuchinski.galeria.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

@WebServlet(name = "RegiesterServlet", value = "/regiester")
public class RegiesterServlet extends HttpServlet {
    DAO db;
    @Override
    public void init() throws ServletException {
        super.init();
        db = new MariaDB_DAOImlp();
        try {
            db.initConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String login = request.getParameter("login");
        String passwd = request.getParameter("password");
        String passwd2 = request.getParameter("password2");
        String email = request.getParameter("email");

        // Service : sprawdzanie danych do rejestracji
        List<String> errors = new ArrayList<String>();
        if (login.isBlank() || passwd.isBlank() || passwd2.isBlank() || email.isBlank()) {
            errors.add("Proszę wypełnić wszystkie pola");
        }
        if (Pattern.compile("[^a-ząęóśłżźćńA-ZĄĘÓŚŁŻŹĆŃ0-9]").matcher(login).find()) {
            errors.add("Podany login nie składa się wyłącznie z liter i cyfr");
        }
        if (login.length() < 6 || login.length() > 20) {
            errors.add("Login powinien mieć długość 6-20 znaków");
        }
        if (!Pattern.compile("[a-ząęóśłżźćń]").matcher(passwd).find()) {
            errors.add("Podane hasło nie zawiera przynajmniej jednej małej litery");
        }
        if (!Pattern.compile("[A-ZĄĘÓŚŁŻŹĆŃ]").matcher(passwd).find()) {
            errors.add("Podane hasło nie zawiera przynajmniej jednej wielkiej litery");
        }
        if (!Pattern.compile("[0-9]").matcher(passwd).find()) {
            errors.add("Podane hasło nie zawiera przynajmniej jednej cyfry");
        }
        if (passwd.length() < 6 || passwd.length() > 20) {
            errors.add("Hasło  ma mieć długość 6-20 znaków");
        }
        if (!Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$").matcher(email).matches()) {
            errors.add("Podany adres e-mail jest niepoprawny");
        }
        if (!passwd.equals(passwd2)) {
            errors.add("Podane hasła się nie zgadzają");
        }
        // Service : end
        if (db.isLoginOccupied(login)) {
            errors.add("Ten login jest już zajęty");
        }

        if (errors.isEmpty()) {
            // Service : hash password
            /*
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            //
            // salt to string
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< salt.length ;i++)
            {
                sb.append(Integer.toString((salt[i] & 0xff) + 0x100, 16).substring(1));
            }
            byte[] saltfromstring = sb.toString().getBytes(StandardCharsets.UTF_8);
            salt = sb.toString().getBytes(StandardCharsets.UTF_8);
            //
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA-512");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            md.update(salt);
            byte[] bytes = md.digest(passwd.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< salt.length ;i++)
            {
                sb.append(Integer.toString((salt[i] & 0xff) + 0x100, 16).substring(1));
            }
            sb.append(':');
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            String hash = sb.toString();
            */
            // Service : end

            //System.out.println(hash);

            try {
                if (db.createUser(login, passwd, email)) {
                    User user = db.login(login, passwd);
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    response.sendRedirect("/");
                    return;
                }
            } catch (Exception e) {
                errors.add(e.getMessage());
            }
        }
        request.setAttribute("R_errors", errors);
        request.getRequestDispatcher("/reg-log.jsp?login="+login+"&email="+email).forward(request,response);
    }
}