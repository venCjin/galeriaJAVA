package com.jsuchinski.galeria.db;

import com.jsuchinski.galeria.model.FotoTileData;
import com.jsuchinski.galeria.model.GalleryTileData;
import com.jsuchinski.galeria.model.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.List;

public interface DAO {
    void initConnection() throws SQLException, ClassNotFoundException;
    boolean isLoginOccupied(String login);
    boolean createUser(String login, String password, String email) throws Exception;
    User login(String login, String password) throws Exception;
    int getAlbumCountForGallery();
    List<GalleryTileData> getAlbumsDataForGallery(String sort, int sqlpage, int tilesPerPage) throws Exception;
    int getFotoCountForAlbum(int idAlbum);
    List<FotoTileData> getFotosDataForAlbum(int idAlbumu, int sqlpage, int tilesPerPage) throws Exception;
    boolean canRateFoto(@Nullable User user, int idZdjecia);
    Object[] getFotoRating(int idZdjecia);
    @Nullable FotoTileData getFotoData(int idZdjecia);
    boolean addFotoRating(int idZdjecia, @Nullable User user, int rating);
    int addAlbum(String albumName, @Nonnull User user);
    List<GalleryTileData> getUserAlbums(@Nonnull User user) throws Exception;
    int addFoto(int idAlbum, String description, @Nonnull User user) throws Exception;
}
