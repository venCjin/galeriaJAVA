package com.jsuchinski.galeria.db;

import com.jsuchinski.galeria.model.FotoTileData;
import com.jsuchinski.galeria.model.GalleryTileData;
import com.jsuchinski.galeria.model.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MariaDB_DAOImlp implements DAO {
    Connection con;

    @Override
    public void initConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/galeria";
//        String user = "user1";
//        String pass = "pass";
        String user = "root";
        String pass = "password";
        con = DriverManager.getConnection(url, user, pass);
    }

    @Override
    public boolean isLoginOccupied(String login) {
        boolean found = false;
        String sql = "SELECT COUNT(*) FROM galeria.uzytkownicy WHERE login = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, login);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                int count = rs.getInt(1);
                if (count > 0) {
                    found = true;
                }
            }
        } catch (SQLException e) {
            //throw new Exception("Błąd bazy danych", e);
            return true;
        }
        return found;
    }

    @Override
    public boolean createUser(String login, String password, String email) throws Exception {
        boolean created = false;
        String sql = "INSERT INTO uzytkownicy (login, email, haslo, zarejestrowany, uprawnienia, aktywny) VALUES(?, ?, ?, CURRENT_DATE, 'uzytkownik', 1)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, email);
            pstmt.setString(3, password);

            created = pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new Exception("Wystąpił błąd przy rejestrowaniu użytkownika.<br>Opis błędu: " + e.getMessage(), e);
        }
        return created;
    }

    @Override
    public User login(String login, String password) throws Exception {
        if (login.isBlank() || password.isBlank()) {
            throw new Exception("Niepoprawne dane logowania");
        }
        String selectSql = "SELECT * FROM galeria.uzytkownicy WHERE login=? AND haslo=?";
        try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return User.fromResultSet(rs);
                } else {
                    throw new Exception("Niepoprawne dane logowania");
                }
            }

        } catch (SQLException e) {
            throw new Exception("Błąd bazy danych", e);
        } catch (ClassNotFoundException e) {
            throw new Exception("Błąd serwera", e);
        }
    }

    @Override
    public int getAlbumCountForGallery() {
        int albumsCount = 0;
        String selectSql = "SELECT COUNT(*) AS liczba_wierszy FROM ( SELECT a.id FROM albumy AS a JOIN zadjecia AS z ON a.id = z.id_albumu WHERE z.zaakceptowane GROUP BY a.id HAVING SUM(z.zaakceptowane) > 0) AS podzapytanie";
        try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                boolean hasData = rs.next();
                if (hasData) albumsCount = rs.getInt("liczba_wierszy");
            }
        } catch (SQLException ignored) {
        }
        return albumsCount;
    }

    @Override
    public List<GalleryTileData> getAlbumsDataForGallery(String sort, int sqlpage, int tilesPerPage) throws Exception {
        List<GalleryTileData> items = new ArrayList<>();
        String selectSql = "SELECT a.id AS id_a, z.id AS id_z, a.tytul AS tytul, a.data AS data, u.login AS login FROM albumy AS a JOIN uzytkownicy AS u ON a.id_uzytkownika=u.id JOIN zadjecia AS z ON a.id=z.id_albumu WHERE z.zaakceptowane GROUP BY a.id ORDER BY "+sort+" limit ?,?";
        try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
            pstmt.setInt(1, sqlpage);
            pstmt.setInt(2, tilesPerPage);
            try (ResultSet rs = pstmt.executeQuery()) {
                //! DEBUG
                //albumsCount = 0;
                while (rs.next()) {
                    //Date d = rs.getDate("data");
                    GalleryTileData data = new GalleryTileData(
                        rs.getInt("id_a"),
                        rs.getInt("id_z"),
                        rs.getString("tytul"),
                        rs.getString("login"),
                        rs.getString("data")
                    );
                    items.add(data);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Błąd bazy danych", e);
        }
        return items;
    }

    @Override
    public int getFotoCountForAlbum(int idAlbum) {
        int photoCount = 0;
        String selectSql =
            "SELECT COUNT(*) AS liczba_wierszy FROM (" +
                " SELECT z.id" +
                " FROM albumy AS a" +
                " JOIN zadjecia AS z" +
                " ON a.id=z.id_albumu" +
                " WHERE z.zaakceptowane AND a.id=?" +
                " GROUP BY z.id" +
                ") AS podzapytanie";
        try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
            pstmt.setInt(1, idAlbum);
            try (ResultSet rs = pstmt.executeQuery()) {
                boolean hasData = rs.next();
                if (hasData) photoCount = rs.getInt("liczba_wierszy");
            }
        } catch (SQLException ignored) {
        }
        return photoCount;
    }

    @Override
    public List<FotoTileData> getFotosDataForAlbum(int idAlbumu, int sqlpage, int tilesPerPage) throws Exception {
        List<FotoTileData> items = new ArrayList<>();
        String selectSql = "SELECT z.id AS id_z, z.tytul AS opis, z.data AS data, u.login AS autor" +
            " FROM albumy AS a" +
            " JOIN zadjecia AS z ON a.id=z.id_albumu" +
            " JOIN uzytkownicy AS u ON z.id_uzytkownika=u.id" +
            " WHERE z.zaakceptowane AND a.id=?" +
            " ORDER BY a.tytul" +
            " limit ?,?";
        try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
            pstmt.setInt(1, idAlbumu);
            pstmt.setInt(2, sqlpage);
            pstmt.setInt(3, tilesPerPage);
            try (ResultSet rs = pstmt.executeQuery()) {
                //! DEBUG
                //albumsCount = 0;
                while (rs.next()) {
                    //Date d = rs.getDate("data");
                    FotoTileData data = new FotoTileData(
                        idAlbumu,
                        rs.getInt("id_z"),
                        rs.getString("opis"),
                        rs.getString("autor"),
                        rs.getString("data")
                    );
                    items.add(data);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Błąd bazy danych", e);
        }
        return items;
    }

    @Override
    public boolean canRateFoto(@Nullable User user, int idZdjecia) {
        if(user != null && user.isActive()) {
            String selectSql = "SELECT *" +
                " FROM zadjecia_oceny" +
                " WHERE id_uzytkownika=?" +
                " AND id_zadjecia=?";
            try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
                pstmt.setInt(1, user.getId());
                pstmt.setInt(2, idZdjecia);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.next()) {
                        return true;
                    }
                }
            } catch (SQLException ignored) {
            }
        }
        return false;
    }

    @Override
    public Object[] getFotoRating(int idZdjecia) {
        Object[] pair =  new Object[2];
        pair[0] = 0.0;
        pair[1] = 0;
        String selectSql = "SELECT AVG(ocena) as avg, COUNT(ocena) as count" +
            " FROM zadjecia_oceny" +
            " WHERE id_zadjecia=?";
        try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
            pstmt.setInt(1, idZdjecia);
            try (ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    pair[0] = rs.getDouble("avg");
                    pair[1] = rs.getInt("count");
                }
            }
        } catch (SQLException ignored) {
        }
        return pair;
    }

    @Nullable
    @Override
    public FotoTileData getFotoData(int idZdjecia) {
        FotoTileData foto = null;
        String selectSql = "SELECT z.tytul, z.id_albumu, z.data, u.login" +
            " FROM zadjecia AS z" +
            " JOIN uzytkownicy AS u ON z.id_uzytkownika=u.id" +
            " WHERE zaakceptowane AND z.id=?";
        try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
            pstmt.setInt(1, idZdjecia);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    //Date d = rs.getDate("data");
                    foto = new FotoTileData(
                        rs.getInt("id_albumu"),
                        idZdjecia,
                        rs.getString("tytul"),
                        rs.getString("login"),
                        rs.getString("data")
                    );
                }
            }
        } catch (SQLException ignored) {
        }
        return foto;
    }

    @Override
    public boolean addFotoRating(int idZdjecia, @Nullable User user, int rating) {
        if (user != null && user.isActive()) {
            String insertSql = "INSERT INTO zadjecia_oceny VALUES (?,?,?)";
            try (PreparedStatement pstmt = con.prepareStatement(insertSql)) {
                pstmt.setInt(1, idZdjecia);
                pstmt.setInt(2, user.getId());
                pstmt.setInt(3, rating);
                return pstmt.executeUpdate() > 0;
                //
            } catch (SQLException ignored) {
            }
        }
        return false;
    }

    @Override
    public int addAlbum(String albumName, @Nonnull User user) {
        String insertSql = "INSERT INTO albumy (tytul, data, id_uzytkownika) VALUES(?, CURRENT_DATE, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, albumName);
            pstmt.setInt(2, user.getId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Dodanie albumu zakończone niepowodzeniem, nie miało to wpływu na żadne wiersze.");
            }
            ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next())
            {
                int last_inserted_id = rs.getInt(1);
                return last_inserted_id;
            } else {
                throw new SQLException("Dodanie albumu zakończone niepowodzeniem, nie udało się uzyskać id albumu.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    @Override
    public List<GalleryTileData> getUserAlbums(@Nonnull User user) throws Exception {
        List<GalleryTileData> items = new ArrayList<>();
        String selectSql = "SELECT * FROM albumy WHERE id_uzytkownika=? ORDER BY tytul";
        try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
            pstmt.setInt(1, user.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                //! DEBUG
                //albumsCount = 0;
                while (rs.next()) {
                    GalleryTileData data = new GalleryTileData(
                        rs.getInt("id"),
                        rs.getString("tytul")
                    );
                    items.add(data);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Błąd bazy danych", e);
        }
        return items;
    }

    @Override
    public int addFoto(int idAlbum, String description, @Nonnull User user) throws Exception {
        boolean defaultAccepted = false;
        String insertSql = "INSERT INTO zadjecia (tytul, data, id_uzytkownika, id_albumu, zaakceptowane) VALUES(?, CURRENT_DATE, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, description);
            pstmt.setInt(2, user.getId());
            pstmt.setInt(3, idAlbum);
            pstmt.setBoolean(4, defaultAccepted);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new Exception("Dodanie zdjęcia zakończone niepowodzeniem, nie miało to wpływu na żadne wiersze.");
            }
            ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next())
            {
                int last_inserted_id = rs.getInt(1);
                return last_inserted_id;
            } else {
                throw new Exception("Dodanie zdjęcia zakończone niepowodzeniem, nie udało się uzyskać id zdjęcia.");
            }
        } catch (SQLException e) {
            throw new Exception("Dodanie zdjęcia zakończone niepowodzeniem.");
        }
    }

    @Override
    public List<FotoTileData> adminGetFotosWithParam(@Nonnull String param) {
        List<FotoTileData> fotos = new ArrayList<>();
        String selectSql = "SELECT id, id_albumu, tytul, data FROM zadjecia "+param+" ORDER BY id";
        try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    //Date d = rs.getDate("data");
                    fotos.add(
                        new FotoTileData(
                            rs.getInt("id_albumu"),
                            rs.getInt("id"),
                            rs.getString("tytul"),
                            null,
                            rs.getString("data")
                        )
                    );
                }
            }
        } catch (SQLException ignored) {
        }

        return fotos;
    }

    @Override
    public boolean acceptFoto(int idFoto) throws Exception {
        boolean updated = false;
        String sql = "UPDATE zadjecia SET zaakceptowane=1 WHERE id=?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, idFoto);

            updated = pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new Exception("Zaakceptowanie zdjęcia nie powiodła się. Spróbuj ponownie.<br>Opis błędu: " + e.getMessage(), e);
        }
        return updated;
    }

    @Override
    public boolean deleteFoto(int idFoto) throws Exception {
        boolean deleted = false;
        String sql = "DELETE FROM zadjecia WHERE id=?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, idFoto);

            deleted = pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new Exception("Usunięcie zdjęcia nie powiodło się. Spróbuj ponownie.<br>Opis błędu: " + e.getMessage(), e);
        }
        return deleted;
    }

    //    public static void main(String[] args) {
//        String url = "jdbc:mysql://localhost:3306/galeria";
//        String user = "user1";
//        String pass = "pass";
//        try (Connection con = getConnection()) {
////        try (Connection con = DriverManager.getConnection(url, user, pass)) {
////            try (Statement stmt = con.createStatement()) {
////                String tableSql = "CREATE TABLE IF NOT EXISTS employees"
////                        + "(emp_id int PRIMARY KEY AUTO_INCREMENT, name varchar(30),"
////                        + "position varchar(30), salary double)";
////                stmt.execute(tableSql);
////            }
//            System.out.println("good");
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
