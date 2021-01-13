package com.nutslaboratory.nutengine.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;


public class AudioManager implements Disposable{
    public static final AudioManager instance = new AudioManager();

    private AudioManager(){}

    private float musicVolume;
    private float soundVolume;

    private boolean haveMusic = true;
    private boolean haveSound = true;

    private Music playingMusic;
    private float playingMusicVolume;

    public long playSound(Sound sound){
        return playSound(sound, 1, false);
    }

    public long playSound(Sound sound, float volume){
        return playSound(sound, volume, false);
    }

    public long playSound(Sound sound, float volume, boolean loop){
        return playSound(sound, volume, loop, false);
    }

    public long playSound(Sound sound, float volume, boolean loop, boolean forcePlay){
        long id = -1;

        if(haveSound || forcePlay){
            if(loop){
                id = sound.loop(soundVolume * volume);
                //Gdx.app.log("test", "loop: "+id);
            }else{
                id = sound.play(soundVolume * volume);
            }
        }else{
            sound.play(0);
        }

        return id;
    }

    public void pauseSound(Sound sound, long id){
        sound.pause(id);
        //Gdx.app.log("test", "pause: "+id);
    }

    public void resumeSound(Sound sound, long id){
        sound.resume(id);
        //Gdx.app.log("test", "resume: "+id);
    }

    public void stopSound(Sound sound, long id){
        sound.stop(id);
        //Gdx.app.log("test", "stop: "+id);
    }

    public void muteSound(){
        haveSound = false;

    }

    public void unmuteSound(){
        haveSound = true;
    }

    public void setVolumeSound(Sound sound, long id, float volume){
        sound.setVolume(id, soundVolume * volume);
    }

    public void playMusic(Music music){
        playMusic(music, 1);
    }

    public void playMusic(Music music, float volume){
        if(playingMusic != null) {
            stopMusic();
        }

        playingMusicVolume = volume;

        playingMusic = music;
        playingMusic.setLooping(true);

        if(haveMusic) {
            playingMusic.setVolume(musicVolume * playingMusicVolume);
            //Gdx.app.log("", "loop: "+musicVolume * playingMusicVolume);
        }else{
            playingMusic.setVolume(0);
        }
        playingMusic.play();
    }

    public void pauseMusic() {
        if(playingMusic == null) return;

        playingMusic.pause();
    }

    public void resumeMusic() {
        if(playingMusic == null) return;

        playingMusic.play();
    }

    public void stopMusic() {
        if(playingMusic == null) return;

        playingMusic.stop();
        playingMusic = null;
    }

    public void muteMusic(){
        haveMusic = false;


        if(playingMusic == null) return;

        playingMusic.setVolume(0);


    }

    public void unmuteMusic(){
        haveMusic = true;


        if(playingMusic == null) return;

        playingMusic.setVolume(musicVolume * playingMusicVolume);
        //Gdx.app.log("", "loop: "+musicVolume * playingMusicVolume);
    }

    public boolean isMusicPlaying(){
        return playingMusic != null ? true : false;
    }

    public void setMusicVolume(float musicVolume){
        this.musicVolume = musicVolume;
    }

    public void setSoundVolume(float soundVolume){
        this.soundVolume = soundVolume;
    }

    @Override
    public void dispose() {
        playingMusic = null;
    }


}


