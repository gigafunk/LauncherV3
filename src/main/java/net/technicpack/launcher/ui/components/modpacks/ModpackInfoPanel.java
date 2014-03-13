/*
 * This file is part of The Technic Launcher Version 3.
 * Copyright (C) 2013 Syndicate, LLC
 *
 * The Technic Launcher is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Technic Launcher  is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * as well as a copy of the GNU Lesser General Public License,
 * along with The Technic Launcher.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.technicpack.launcher.ui.components.modpacks;

import net.technicpack.launcher.lang.ResourceLoader;
import net.technicpack.launcher.ui.LauncherFrame;
import net.technicpack.launcher.ui.controls.*;
import net.technicpack.launcher.ui.controls.feeds.FeedItem;
import net.technicpack.launcher.ui.controls.feeds.HorizontalGallery;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ModpackInfoPanel extends JPanel {
    private ResourceLoader resources;

    private BufferedImage defaultImage;

    private TiledBackground background;
    private HorizontalGallery feedGallery;

    public ModpackInfoPanel(ResourceLoader loader) {
        this.resources  = loader;

        initComponents();
    }

    protected void clickLeftFeedButton() {
        feedGallery.selectPreviousComponent();
    }

    protected void clickRightFeedButton() {
        feedGallery.selectNextComponent();
    }

    protected void clickFeedItem(FeedItem item) {
        if (!feedGallery.selectComponent(item)) {
            try {
                Desktop.getDesktop().browse(new URI(item.getUrl()));
            } catch (IOException ex) {
                //Thrown by Desktop.browse() - just log & ignore
                ex.printStackTrace();
            } catch (URISyntaxException ex) {
                //If we got a bogus URL from the internet, then this will throw.  Log & Ignore
                ex.printStackTrace();
            } catch (RuntimeException ex) {
                //browse() throws a bunch of runtime exceptions if you give it bad input
                //WHICH IS AWESOME
                ex.printStackTrace();
            }
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        defaultImage = resources.getImage("background_repeat2.png");
        background = new TiledBackground(defaultImage);
        background.setLayout(new BorderLayout());
        background.setForeground(LauncherFrame.COLOR_WHITE_TEXT);
        this.add(background, BorderLayout.CENTER);

        JPanel layoutPanel = new JPanel();
        layoutPanel.setOpaque(false);
        layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.PAGE_AXIS));
        layoutPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        background.add(layoutPanel,BorderLayout.CENTER);

        ModpackBanner modpackBanner = new ModpackBanner(resources);
        modpackBanner.setBackground(LauncherFrame.COLOR_BANNER);
        modpackBanner.setBorder(BorderFactory.createEmptyBorder(0,0,4,0));
        layoutPanel.add(modpackBanner);

        JPanel rootFeedPanel = new JPanel();
        BorderLayout rootFeedLayout = new BorderLayout();
        rootFeedLayout.setVgap(10);
        rootFeedPanel.setLayout(rootFeedLayout);
        rootFeedPanel.setOpaque(false);
        rootFeedPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 20, 16));
        layoutPanel.add(rootFeedPanel);

        ModpackDataDisplay modpackDataDisplay = new ModpackDataDisplay(resources);
        rootFeedPanel.add(modpackDataDisplay, BorderLayout.PAGE_START);

        JPanel feedBottom = new JPanel();
        feedBottom.setOpaque(false);
        feedBottom.setLayout(new GridBagLayout());
        rootFeedPanel.add(feedBottom, BorderLayout.CENTER);

        JPanel topline = new JPanel();
        topline.setOpaque(false);
        topline.setLayout(new BoxLayout(topline, BoxLayout.LINE_AXIS));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 0.0;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        feedBottom.add(topline, constraints);

        JLabel toplineLabel = new AAJLabel(resources.getString("launcher.packfeed.title"));
        toplineLabel.setFont(resources.getFont(ResourceLoader.FONT_RALEWAY, 28));
        toplineLabel.setForeground(LauncherFrame.COLOR_WHITE_TEXT);
        topline.add(toplineLabel);
        topline.add(Box.createHorizontalGlue());

        JButton leftButton = new JButton(resources.getIcon("status_left.png"));
        leftButton.setBorder(BorderFactory.createEmptyBorder());
        leftButton.setContentAreaFilled(false);
        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickLeftFeedButton();
            }
        });
        topline.add(leftButton);

        JButton rightButton = new JButton(resources.getIcon("status_right.png"));
        rightButton.setBorder(BorderFactory.createEmptyBorder());
        rightButton.setContentAreaFilled(false);
        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickRightFeedButton();
            }
        });
        topline.add(rightButton);

        feedGallery = new HorizontalGallery();
        feedGallery.setBackground(LauncherFrame.COLOR_BANNER);
        feedGallery.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 0.0;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.ipady = 150;
        constraints.fill = GridBagConstraints.BOTH;
        feedBottom.add(feedGallery, constraints);

        for (int i = 0; i < 10; i++) {
            FeedItem item = new FeedItem(resources, "FARTS FARTS FARTS AND FARTS FARTS FARTS AND FARTS FARTS FARTS AND FARTS FARTS FARTS AND FARTS FARTS FARTS AND FARTS FARTS FARTS AND FARTS FARTS FARTS AND FARTS FARTS FARTS AND FARTS FARTS FARTS AND FARTS FARTS FARTS AND FARTS FARTS FARTS AND FARTS FARTS FARTS AND FARTS FARTS FARTS AND FARTS FARTS FARTS AND FARTS FARTS FARTS AND FARTS FARTS FARTS AND FARTS FARTS FARTS AND FARTS FARTS FARTS AND FARTS FARTS FARTS AND ", "http://www.technicpack.net/", "sct", resources.getImage("news/AuthorAvatar.jpg"));
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clickFeedItem((FeedItem)e.getSource());
                }
            });
            feedGallery.add(item);
        }

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        Component vertFill = Box.createGlue();
        feedBottom.add(vertFill, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.weightx = 1.0;
        constraints.weighty = 0.0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        Component horizFill = Box.createGlue();
        feedBottom.add(horizFill, constraints);

        RectButton playButton = new RectButton(resources.getString("launcher.pack.launch"));
        playButton.setPreferredSize(new Dimension(295, 40));
        playButton.setMinimumSize(new Dimension(295, 40));
        playButton.setFont(resources.getFont(ResourceLoader.FONT_RALEWAY, 24));
        playButton.setBorder(BorderFactory.createEmptyBorder());
        playButton.setForeground(LauncherFrame.COLOR_WHITE_TEXT);
        playButton.setBackground(LauncherFrame.COLOR_BLUE);
        playButton.setHoverBackground(LauncherFrame.COLOR_BLUE_DARKER);
        playButton.setAlignmentX(RIGHT_ALIGNMENT);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        feedBottom.add(playButton, constraints);
    }
}