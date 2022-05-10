package com.streletsa.memedealer.tgpublisher;

import com.streletsa.memedealer.tgpublisher.subscriber.MemeSubscriber;

public class Main {

    public static void main(String[] args) {
        MemeSubscriber memeSubscriber = new MemeSubscriber();
        memeSubscriber.start();
    }

}
