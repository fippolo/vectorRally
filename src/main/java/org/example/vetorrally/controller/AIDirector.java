package org.example.vetorrally.controller;

import org.example.vetorrally.model.Bot;
import org.example.vetorrally.model.Track;
import org.example.vetorrally.model.TrackElement;
import org.example.vetorrally.model.Vector2D;
import org.example.vetorrally.view.Renderer;

import java.util.List;

public class AIDirector {
    private Renderer renderer;
    private Track gameTrack;
    private List<Bot> bots;
    public AIDirector(Track gameTrack, List<Bot> bots, Renderer renderer) {
        this.gameTrack = gameTrack;
        this.bots = bots;
        this.renderer = renderer;
    }
    public void moveBots(){
        for (Bot bot : bots) {
            renderer.clearOcucpiedSpace(bot.getPosition());
            Vector2D nextPos= bot.findNextMove();
            bot.moveTo(nextPos);
            renderer.setOccupied(bot.getPosition());
        }
    }



}
