import java.util.ArrayList;
import java.util.Scanner;

class Song {
    private String title;
    private String artist;
    private String genre;
    private double runtime;

    public Song(String title, String artist, String genre, double runtime) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.runtime = runtime;
    }

    public String getTitle() {
        return title;
    }
    public String getArtist() {
        return artist;
    }
    public String getGenre() {
        return genre;
    }
    public double getRuntime() {
        return runtime;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public void setRuntime(double runtime) {
        this.runtime = runtime;
    }

    @Override
    public String toString() {
        return title + " by " + artist + " [" + genre + "] - " + runtime + " min";
    }
}

class PlaylistManager {
    private ArrayList<Song> playlist;

    public PlaylistManager() {
        playlist = new ArrayList<>();
    }

    public void addSong(Song song) {
        playlist.add(song);
        System.out.println("Song added: " + song);
    }

    public void removeSong(String title) {
        boolean removed = false;
        for (int i = 0; i < playlist.size(); i++) {
            if (playlist.get(i).getTitle().equalsIgnoreCase(title)) {
                System.out.println("Removed song: " + playlist.get(i));
                playlist.remove(i);
                removed = true;
                break;
            }
        }
        if (!removed) {
            System.out.println("Song with title \"" + title + "\" not found.");
        }
    }

    public void updateSong(String title, String newTitle, String newArtist, String newGenre, double newRuntime) {
        boolean updated = false;
        for (Song song : playlist) {
            if (song.getTitle().equalsIgnoreCase(title)) {
                song.setTitle(newTitle);
                song.setArtist(newArtist);
                song.setGenre(newGenre);
                song.setRuntime(newRuntime);
                System.out.println("Updated song: " + song);
                updated = true;
                break;
            }
        }
        if (!updated) {
            System.out.println("Song with title \"" + title + "\" not found.");
        }
    }

    public ArrayList<Song> generatePlaylistByArtist(String artist) {
        ArrayList<Song> result = new ArrayList<>();
        for (Song song : playlist) {
            if (song.getArtist().equalsIgnoreCase(artist)) {
                result.add(song);
            }
        }
        return result;
    }

    public ArrayList<Song> generatePlaylistByGenre(String genre) {
        ArrayList<Song> result = new ArrayList<>();
        for (Song song : playlist) {
            if (song.getGenre().equalsIgnoreCase(genre)) {
                result.add(song);
            }
        }
        return result;
    }

    public void displayPlaylist(ArrayList<Song> songs) {
        if (songs.isEmpty()) {
            System.out.println("No songs to display.");
            return;
        }
        double totalRuntime = 0;
        for (Song song : songs) {
            System.out.println(song);
            totalRuntime += song.getRuntime();
        }
        System.out.printf("Total Runtime: %.2f minutes%n", totalRuntime);
    }

    public Song searchSongByTitle(String title) {
        for (Song song : playlist) {
            if (song.getTitle().equalsIgnoreCase(title)) {
                return song;
            }
        }
        return null;
    }

    public ArrayList<Song> searchSongsByArtist(String artist) {
        ArrayList<Song> result = new ArrayList<>();
        for (Song song : playlist) {
            if (song.getArtist().equalsIgnoreCase(artist)) {
                result.add(song);
            }
        }
        return result;
    }
}

public class PlaylistApp {
    public static void main(String[] args) {
        PlaylistManager manager = new PlaylistManager();
        Scanner scanner = new Scanner(System.in);

        // Example songs
        manager.addSong(new Song("Bohemian Rhapsody", "Queen", "Rock", 5.55));
        manager.addSong(new Song("Shape of You", "Ed Sheeran", "Pop", 4.24));
        manager.addSong(new Song("Blinding Lights", "The Weeknd", "Pop", 3.50));
        manager.addSong(new Song("Mind Your Manners", "Pearl Jam", "Grunge", 2.38));
        manager.addSong(new Song("Hotel California", "Eagles", "Rock", 6.30));
        manager.addSong(new Song("Stairway to Heaven", "Led Zeppelin", "Rock", 8.02));
        manager.addSong(new Song("Imagine", "John Lennon", "Pop", 3.03));
        manager.addSong(new Song("Smells Like Teen Spirit", "Nirvana", "Grunge", 5.01));
        manager.addSong(new Song("Billie Jean", "Michael Jackson", "Pop", 4.54));
        manager.addSong(new Song("Like a Rolling Stone", "Bob Dylan", "Rock", 6.13));
        manager.addSong(new Song("Purple Haze", "Jimi Hendrix", "Rock", 2.50));
        manager.addSong(new Song("Hey Jude", "The Beatles", "Rock", 7.11));
        manager.addSong(new Song("Superstition", "Stevie Wonder", "Funk", 4.26));
        manager.addSong(new Song("Livin' on a Prayer", "Bon Jovi", "Rock", 4.10));
        manager.addSong(new Song("Sweet Child o' Mine", "Guns N' Roses", "Rock", 5.56));
        manager.addSong(new Song("Rolling in the Deep", "Adele", "Pop", 3.48));
        manager.addSong(new Song("Uptown Funk", "Mark Ronson ft. Bruno Mars", "Funk", 4.29));
        manager.addSong(new Song("Take On Me", "A-ha", "Synth-pop", 3.45));
        manager.addSong(new Song("Boogie Wonderland", "Earth, Wind & Fire", "Disco", 4.48));
        manager.addSong(new Song("Shallow", "Lady Gaga & Bradley Cooper", "Pop", 3.37));
        manager.addSong(new Song("Somebody to Love", "Queen", "Rock", 4.56));
        manager.addSong(new Song("Radioactive", "Imagine Dragons", "Alternative Rock", 3.07));
        manager.addSong(new Song("Shake It Off", "Taylor Swift", "Pop", 3.39));
        manager.addSong(new Song("Let It Be", "The Beatles", "Rock", 4.03));
        manager.addSong(new Song("Poker Face", "Lady Gaga", "Pop", 3.57));
        manager.addSong(new Song("Thunderstruck", "AC/DC", "Rock", 4.52));
        manager.addSong(new Song("Lose Yourself", "Eminem", "Hip-Hop", 5.26));
        manager.addSong(new Song("Even Flow", "Pearl Jam", "Grunge", 4.52));
        manager.addSong(new Song("Counting Stars", "OneRepublic", "Pop Rock", 4.17));
        manager.addSong(new Song("Creep", "Radiohead", "Alternative Rock", 3.58));
        manager.addSong(new Song("Mr. Brightside", "The Killers", "Rock", 3.42));
        manager.addSong(new Song("Viva La Vida", "Coldplay", "Alternative", 4.02));
        manager.addSong(new Song("Don't Stop Believin'", "Journey", "Rock", 4.11));
        manager.addSong(new Song("Seven Nation Army", "The White Stripes", "Rock", 3.52));
        manager.addSong(new Song("Wonderwall", "Oasis", "Britpop", 4.18));
        manager.addSong(new Song("We Will Rock You", "Queen", "Rock", 2.02));
        manager.addSong(new Song("Hallelujah", "Leonard Cohen", "Folk", 6.45));
        manager.addSong(new Song("Roxanne", "The Police", "Rock", 3.12));
        manager.addSong(new Song("Beat It", "Michael Jackson", "Pop", 4.18));
        manager.addSong(new Song("Zombie", "The Cranberries", "Alternative", 5.06));
        manager.addSong(new Song("Eye of the Tiger", "Survivor", "Rock", 4.05));
        manager.addSong(new Song("Feel Good Inc.", "Gorillaz", "Alternative", 3.42));
        manager.addSong(new Song("Enter Sandman", "Metallica", "Metal", 5.31));
        manager.addSong(new Song("Clocks", "Coldplay", "Alternative", 5.07));
        manager.addSong(new Song("All Star", "Smash Mouth", "Rock", 3.20));
        manager.addSong(new Song("Boulevard of Broken Dreams", "Green Day", "Rock", 4.22));
        manager.addSong(new Song("Take Me to Church", "Hozier", "Indie", 4.02));
        manager.addSong(new Song("Happy", "Pharrell Williams", "Pop", 3.53));
        manager.addSong(new Song("Chasing Cars", "Snow Patrol", "Alternative", 4.27));
        manager.addSong(new Song("American Idiot", "Green Day", "Punk Rock", 2.54));
        manager.addSong(new Song("In the End", "Linkin Park", "Rock", 3.36));
        manager.addSong(new Song("Heroes", "David Bowie", "Rock", 6.07));
        manager.addSong(new Song("Smooth", "Santana ft. Rob Thomas", "Rock", 4.56));
        manager.addSong(new Song("What a Wonderful World", "Louis Armstrong", "Jazz", 2.21));
        manager.addSong(new Song("I Will Survive", "Gloria Gaynor", "Disco", 3.15));
        manager.addSong(new Song("Dancing Queen", "ABBA", "Disco", 3.50));
        manager.addSong(new Song("Kashmir", "Led Zeppelin", "Rock", 8.37));
        manager.addSong(new Song("Sweet Home Alabama", "Lynyrd Skynyrd", "Southern Rock", 4.43));
        manager.addSong(new Song("Born to Run", "Bruce Springsteen", "Rock", 4.30));
        manager.addSong(new Song("Black Hole Sun", "Soundgarden", "Grunge", 5.18));
        manager.addSong(new Song("Time", "Pink Floyd", "Rock", 7.05));
        manager.addSong(new Song("No One Knows", "Queens of the Stone Age", "Rock", 4.38));
        manager.addSong(new Song("Bad Guy", "Billie Eilish", "Pop", 3.14));
        manager.addSong(new Song("Uprising", "Muse", "Alternative", 5.04));
        manager.addSong(new Song("Reptilia", "The Strokes", "Rock", 3.40));
        manager.addSong(new Song("Somewhere Only We Know", "Keane", "Alternative", 3.57));
        manager.addSong(new Song("Love Yourself", "Justin Bieber", "Pop", 3.53));
        manager.addSong(new Song("The Middle", "Jimmy Eat World", "Alternative", 2.49));
        manager.addSong(new Song("Don't Let the Sun Go Down on Me", "Elton John", "Rock", 5.48));
        manager.addSong(new Song("The Sound of Silence", "Simon & Garfunkel", "Folk", 3.05));
        manager.addSong(new Song("We Are the Champions", "Queen", "Rock", 2.59));
        manager.addSong(new Song("Piano Man", "Billy Joel", "Rock", 5.40));
        manager.addSong(new Song("Africa", "Toto", "Rock", 4.55));
        manager.addSong(new Song("Shut Up and Dance", "WALK THE MOON", "Pop Rock", 3.19));
        manager.addSong(new Song("Hey, Soul Sister", "Train", "Pop", 3.36));
        manager.addSong(new Song("Stayin' Alive", "Bee Gees", "Disco", 4.45));
        manager.addSong(new Song("Shake It Out", "Florence + The Machine", "Alternative", 4.37));
        manager.addSong(new Song("Eleanor Rigby", "The Beatles", "Rock", 2.08));
        manager.addSong(new Song("Just the Way You Are", "Bruno Mars", "Pop", 3.40));
        manager.addSong(new Song("I Want It That Way", "Backstreet Boys", "Pop", 3.33));
        manager.addSong(new Song("Break on Through", "The Doors", "Rock", 2.29));
        manager.addSong(new Song("Come Together", "The Beatles", "Rock", 4.19));
        manager.addSong(new Song("Thunder", "Imagine Dragons", "Pop", 3.07));
        manager.addSong(new Song("FORMIDABLE", "Tiakola", "Pop", 2.30));



        boolean running = true;
        while (running) {
            System.out.println("\nPlaylist Manager:");
            System.out.println("1. Add Song");
            System.out.println("2. Remove Song");
            System.out.println("3. Update Song");
            System.out.println("4. Generate Playlist by Artist");
            System.out.println("5. Generate Playlist by Genre");
            System.out.println("6. Search for a Song by Title");
            System.out.println("7. Search for Songs by Artist");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: {
                    System.out.print("Enter song title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter artist: ");
                    String artist = scanner.nextLine();
                    System.out.print("Enter genre: ");
                    String genre = scanner.nextLine();
                    System.out.print("Enter runtime (in minutes): ");
                    double runtime = scanner.nextDouble();
                    scanner.nextLine();
                    manager.addSong(new Song(title, artist, genre, runtime));
                    break;
                }
                case 2: {
                    System.out.print("Enter the title of the song to remove: ");
                    String title = scanner.nextLine();
                    manager.removeSong(title);
                    break;
                }
                case 3: {
                    System.out.print("Enter the title of the song to update: ");
                    String oldTitle = scanner.nextLine();
                    System.out.print("Enter new title: ");
                    String newTitle = scanner.nextLine();
                    System.out.print("Enter new artist: ");
                    String newArtist = scanner.nextLine();
                    System.out.print("Enter new genre: ");
                    String newGenre = scanner.nextLine();
                    System.out.print("Enter new runtime (in minutes): ");
                    double newRuntime = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    manager.updateSong(oldTitle, newTitle, newArtist, newGenre, newRuntime);
                    break;
                }
                case 4: {
                    System.out.print("Enter artist: ");
                    String artist = scanner.nextLine();
                    ArrayList<Song> artistPlaylist = manager.generatePlaylistByArtist(artist);
                    System.out.println("Playlist for artist \"" + artist + "\":");
                    manager.displayPlaylist(artistPlaylist);
                    break;
                }
                case 5: {
                    System.out.print("Enter genre: ");
                    String genre = scanner.nextLine();
                    ArrayList<Song> genrePlaylist = manager.generatePlaylistByGenre(genre);
                    System.out.println("Playlist for genre \"" + genre + "\":");
                    manager.displayPlaylist(genrePlaylist);
                    break;
                }
                case 6: {
                    System.out.print("Enter song title to search: ");
                    String title = scanner.nextLine();
                    Song foundSong = manager.searchSongByTitle(title);
                    if (foundSong != null) {
                        System.out.println("Found: " + foundSong);
                    } else {
                        System.out.println("Song not found.");
                    }
                    break;
                }
                case 7: {
                    System.out.print("Enter artist to search: ");
                    String artist = scanner.nextLine();
                    ArrayList<Song> songsByArtist = manager.searchSongsByArtist(artist);
                    if (songsByArtist.isEmpty()) {
                        System.out.println("No songs found by " + artist);
                    } else {
                        System.out.println("Songs by " + artist + ":");
                        manager.displayPlaylist(songsByArtist);
                    }
                    break;
                }
                case 8: {
                    running = false;
                    System.out.println("Exiting Playlist Manager.");
                    break;
                }
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
        scanner.close();
    }
}

