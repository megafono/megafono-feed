(ns megafono-feed.views.helpers.url-test
  (:require [clojure.test :refer :all]
            [megafono-feed.views.helpers.url :refer :all]))

(defrecord Subscription [plan_uid])
(defrecord Episode [id slug media_provider media_url media_uploaded artwork nid])
(defrecord Channel [id slug subscriptions artwork])

(def subscription (Subscription. "goodbye-manual-feed"))

(def channel (Channel. "x" "xpto-123" nil "art.png"))
(def channel-goodbye-manual-feed (Channel. "y" "yx-321" [subscription] nil))

(def episode-soundcloud (Episode. "1" "episode-1" "soundcloud" "http://soundcloud.com/1234" nil nil nil))
(def episode-external (Episode. "2" "episode-2" "external" "http://archive.org/audio.mp3" nil nil nil))
(def episode-s3 (Episode. "3" "episode-3" "s3" nil nil nil 1))
(def episode-s3-media-uploaded (Episode. "4" "episode-4" "s3" nil "xpto.mp3" nil nil))
(def episode-artwork (Episode. "5" nil nil nil nil "art-ep.png" nil))

(deftest build-episode-media-url-test
  (testing "Soundcloud Provider"
           (is (= "http://soundcloud.com/1234?client_id="
                  (build-episode-media-url episode-soundcloud channel))))
  (testing "External Provider"
           (is (= "https://www.megafono.host/podcast/xpto-123/episode-2.mp3?source=feed"
                  (build-episode-media-url episode-external channel))))
  (testing "Goodbye Feed - External Provider "
           (is (= "http://archive.org/audio.mp3"
                  (build-episode-media-url episode-external channel-goodbye-manual-feed))))
  (testing "S3 Provider"
           (is (= "https://d1r52u2wzcgg4y.cloudfront.net/3/audio.mp3?source=feed"
                  (build-episode-media-url episode-s3 channel))))
  (testing "Media Uploaded - S3 Provider"
           (is (= "https://d1r52u2wzcgg4y.cloudfront.net/4/xpto.mp3?source=feed"
                  (build-episode-media-url episode-s3-media-uploaded channel)))))

(deftest build-episode-embed-url-test
  (testing "build-episode-embed-url"
           (is (= "https://www.megafono.host/podcast/xpto-123/e/episode-3"
                  (build-episode-embed-url episode-s3 channel)))))

(deftest build-episode-image-url-test
  (testing "Episode without Artwork"
           (is (= "https://d17choic6g575e.cloudfront.net/channel/artwork/x/art.png"
                  (build-episode-image-url episode-s3 channel))))
  (testing "Episode with Artwork"
           (is (= "https://d17choic6g575e.cloudfront.net/episode/artwork/5/art-ep.png"
                  (build-episode-image-url episode-artwork channel)))))

(deftest build-image-url-test
  (testing "By default returns channel image"
           (is (= "https://d17choic6g575e.cloudfront.net/channel/artwork/c-123/art.png"
                  (build-image-url "c-123" "art.png"))))
  (testing "Receive model and mounted-as"
           (is (= "https://d17choic6g575e.cloudfront.net/episode/artwork-beta/c-123/art.png"
                  (build-image-url "episode" "artwork-beta" "c-123" "art.png")))))

(deftest build-episode-url-test
  (testing "build-episode-url"
           (is (= "http://mfn.bz/bg"
                  (build-episode-url episode-s3)))))

(deftest build-site-url-test
  (testing "build-site-url"
           (is (= "https://www.megafono.host/podcast/episode-11"
                  (build-site-url "episode-11")))))

(deftest buid-xml-url-test
  (testing "buid-xml-url"
           (is (= "https://feed.megafono.host/episode-12"
                  (buid-xml-url "episode-12")))))