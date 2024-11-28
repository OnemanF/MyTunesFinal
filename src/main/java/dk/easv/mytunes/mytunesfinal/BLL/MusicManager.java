package dk.easv.mytunes.mytunesfinal.BLL;

import dk.easv.mytunes.mytunesfinal.DAO.ISongDataAccess;
import dk.easv.mytunes.mytunesfinal.DAO.db.SongDAO_DB;

import java.io.IOException;

public class MusicManager {

    private ISongDataAccess songDAO;

    public MusicManager() throws IOException {
        songDAO = new SongDAO_DB();
    }
}
