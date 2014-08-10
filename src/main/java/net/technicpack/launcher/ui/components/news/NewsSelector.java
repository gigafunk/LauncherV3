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
package net.technicpack.launcher.ui.components.news;

import net.technicpack.launcher.lang.ResourceLoader;
import net.technicpack.launcher.ui.LauncherFrame;
import net.technicpack.launcher.ui.controls.SimpleScrollbarUI;
import net.technicpack.launcher.ui.controls.TiledBackground;
import net.technicpack.launcher.ui.controls.feeds.NewsWidget;
import net.technicpack.launchercore.image.ImageRepository;
import net.technicpack.platform.IPlatformApi;
import net.technicpack.platform.io.AuthorshipInfo;
import net.technicpack.platform.io.NewsArticle;
import net.technicpack.platform.io.NewsData;
import net.technicpack.rest.RestfulAPIException;
import net.technicpack.utilslib.Utils;
import sun.tools.jar.resources.jar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;

public class NewsSelector extends JPanel {
    private ResourceLoader resources;
    private IPlatformApi platformApi;
    private NewsWidget selectedItem;
    private JPanel widgetHost;

    private NewsInfoPanel panel;

    private ImageRepository<AuthorshipInfo> avatarRepo;

    public NewsSelector(ResourceLoader resources, NewsInfoPanel panel, IPlatformApi platformApi, ImageRepository<AuthorshipInfo> avatarRepo) {
        this.resources = resources;
        this.platformApi = platformApi;
        this.avatarRepo = avatarRepo;
        this.panel = panel;

        initComponents();
        downloadItems();
    }

    protected void selectNewsItem(NewsWidget widget) {
        if (selectedItem != null)
            selectedItem.setIsSelected(false);
        selectedItem = widget;

        if (selectedItem != null)
            selectedItem.setIsSelected(true);

        panel.setArticle(selectedItem.getArticle());
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(LauncherFrame.COLOR_SELECTOR_BACK);

        widgetHost = new JPanel();
        widgetHost.setOpaque(false);
        widgetHost.setLayout(new GridBagLayout());

        JScrollPane scrollPane = new JScrollPane(widgetHost, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUI(new SimpleScrollbarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10,10));
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        add(scrollPane, BorderLayout.CENTER);

        GridBagConstraints constraints = new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0);

        constraints.weighty = 1.0;
        widgetHost.add(Box.createGlue(), constraints);
    }

    protected void loadNewsItems(NewsData news) {

        Collections.sort(news.getArticles(), new Comparator<NewsArticle>() {
            @Override
            public int compare(NewsArticle o1, NewsArticle o2) {
                if (o1.getDate().getTime() > o2.getDate().getTime())
                    return -1;
                else if (o1.getDate().getTime() < o2.getDate().getTime())
                    return 1;
                else
                    return 0;
            }
        });

        widgetHost.removeAll();

        GridBagConstraints constraints = new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0);

        for (int i = 0; i < news.getArticles().size(); i++) {
            NewsWidget widget = new NewsWidget(resources, news.getArticles().get(i), avatarRepo.startImageJob(news.getArticles().get(i).getAuthorshipInfo()));
            widget.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() instanceof NewsWidget)
                        selectNewsItem((NewsWidget)e.getSource());
                }
            });
            widgetHost.add(widget, constraints);
            constraints.gridy++;

            if (selectedItem == null)
                selectNewsItem(widget);
        }

        constraints.weighty = 1.0;
        widgetHost.add(Box.createGlue(), constraints);
    }

    private void downloadItems() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loadNewsItems(platformApi.getNews());
                } catch (RestfulAPIException ex) {
                    Utils.getLogger().log(Level.WARNING, "Unable to load news", ex);
                }
            }
        });

        thread.start();
    }
}
