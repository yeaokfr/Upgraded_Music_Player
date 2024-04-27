import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    //Class Variables
    private Clip clip;
    private Timer timer;
    private JLabel currentTime;
    private JLabel totalTime;
    //private variable time, used long data type because that's what it saves as
    private long time = 0;
    private String playingSong;

    //constructor - how we build the music player
    public MusicPlayer(){
        //Song Options
        String [] songList = {"Dumbbells.wav", "Hey Papi.wav", "Code Kings.wav", "Fire in My Belly.wav", "For Loop.wav", "GORG.wav", "GUI Mastermind.wav", "Hashin_ in the Code.wav",
                "GUI Mastermind.wav", "Mr. Scott.wav", "Programming.wav", "The Boolean Blues.wav", "The Codebreaker_s Fury.wav"};

        //Frame
        JFrame frame = new JFrame("Music Player");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        //Buttons
        JPanel buttonPanel = new JPanel();
        JButton playButton = new JButton("Play");
        JButton pauseButton = new JButton("Pause");
        JButton rewindButton = new JButton("-15s");
        JButton forwardButton = new JButton("+15s");
        buttonPanel.add(rewindButton);
        buttonPanel.add(playButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(forwardButton);

        //Labels
        JPanel labelPanel = new JPanel();
        currentTime = new JLabel("Current Time: 0s");
        totalTime = new JLabel("Total Time: 0s");
        labelPanel.add(currentTime);
        labelPanel.add(totalTime);

        //Dropdown
        JPanel dropDown = new JPanel();
        JComboBox<String> songSelector = new JComboBox<>(songList);
        dropDown.add(songSelector);

        //Button Function
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                //tells program what song to play
                if (clip == null) {
                    playMusic(songSelector.getSelectedItem().toString());
                    startTimer();
                    updateLabels();
                    playingSong = songSelector.getSelectedItem().toString();
                } else if (!clip.isRunning()) {
                    if (songSelector.getSelectedItem().toString().equals(playingSong)) {
                        if (clip.getMicrosecondPosition() == clip.getMicrosecondLength()) {
                            playMusic(songSelector.getSelectedItem().toString());
                            clip.setMicrosecondPosition(0);
                            startTimer();
                            updateLabels();
                            playingSong = songSelector.getSelectedItem().toString();
                        } else {
                            playMusic(songSelector.getSelectedItem().toString());
                            startTimer();
                            updateLabels();
                            playingSong = songSelector.getSelectedItem().toString();
                        }
                    } else {
                        playMusic(songSelector.getSelectedItem().toString());
                        clip.setMicrosecondPosition(0);
                        startTimer();
                        updateLabels();
                        playingSong = songSelector.getSelectedItem().toString();
                    }
                } else {
                    clip.stop();
                    playMusic(songSelector.getSelectedItem().toString());
                    clip.setMicrosecondPosition(0);
                    startTimer();
                    updateLabels();
                    playingSong = songSelector.getSelectedItem().toString();
                }
            }
        });

        forwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                //moves the song forward by 15s
                forward(songSelector.getSelectedItem().toString());
                time = clip.getMicrosecondPosition();
            }
        });

        rewindButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                //moves the song backward by 15s
                rewind(songSelector.getSelectedItem().toString());
                time = clip.getMicrosecondPosition();
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){;
                pauseMusic(songSelector.getSelectedItem().toString());
                stopTimer();
            }
        });

        //Main Panel Order
        panel.add(dropDown);
        panel.add(buttonPanel);
        panel.add(labelPanel);

        //Make Frame Visible
        frame.add(panel);
        frame.setVisible(true);
    }

    public void forward(String selectedSong){
        if (clip.getMicrosecondPosition() + 15000000 > clip.getMicrosecondLength()) {
            clip.setMicrosecondPosition(clip.getMicrosecondLength());
            updateLabels();
        } else {
            clip.setMicrosecondPosition(time + 15000000);
        }
    }

    public void rewind(String selectedSong){
        clip.setMicrosecondPosition(time - 15000000);
        if (clip.getMicrosecondPosition() - 15000000 < 0) {
            clip.setMicrosecondPosition(0);
            updateLabels();
        } else {
            clip.setMicrosecondPosition(time - 15000000);
        }
    }

    public void playMusic(String selectedSong){
        try {
            File file = new File(System.getProperty("user.dir") + "\\src\\audio\\" + selectedSong);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            //sets the time of the song, so it can play where it was previously paused
            clip.setMicrosecondPosition(time);
            clip.start();
        }
        catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void pauseMusic(String selectedSong){
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void startTimer(){
        timer = new Timer(1000, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                updateLabels();
            }
        });
        timer.start();
    }

    public void updateLabels(){
        if (clip != null && clip.isOpen()) {
            if (clip.isRunning()) {
                currentTime.setText("Current Time: " + clip.getMicrosecondPosition()/1000000 + "s");
                totalTime.setText("Total Time: " + clip.getMicrosecondLength()/1000000 + "s");
                time = clip.getMicrosecondPosition();
            }
        }
    }

    public void stopTimer(){
        if (timer != null) {
            //below saves the time of the music when stopped
            time = clip.getMicrosecondPosition();
            timer.stop();
        }
    }

    public static void main(String[] args) {
        new MusicPlayer();
    }
}