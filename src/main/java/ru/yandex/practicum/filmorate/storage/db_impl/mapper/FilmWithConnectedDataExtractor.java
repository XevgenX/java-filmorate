package ru.yandex.practicum.filmorate.storage.db_impl.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FilmWithConnectedDataExtractor implements ResultSetExtractor<List<Film>> {

    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Film> map = new HashMap<>();
        Film film = null;
        while (rs.next()) {
            Long id = rs.getLong("id");
            film = map.get(id);
            if (film == null) {
                film = new Film();
                film.setId(id);
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                Date releaseDate = rs.getDate("release_date");
                if (Objects.nonNull(releaseDate)) {
                    film.setReleaseDate(releaseDate.toLocalDate());
                }
                film.setDuration(rs.getInt("duration"));
                Mpa mpa = new Mpa();
                mpa.setId(rs.getLong("mpaa_id"));
                film.setMpa(mpa);
                film.setLikes(new HashSet<>());
                map.put(id, film);
            }
            long userId = rs.getLong("user_that_liked");
            if (userId > 0) {
                film.addLike(userId);
            }
            long genreId = rs.getLong("genre_id");
            String genreName = rs.getString("genre_name");
            if (genreId > 0 && Objects.nonNull(genreName)) {
                Genre genre = new Genre();
                genre.setId(genreId);
                genre.setName(genreName);
                film.addGenre(genre);
            }
        }
        return new ArrayList<>(map.values());
    }
}
