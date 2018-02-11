(ns megafono-feed.views.helpers.xml-test
  (:require [clojure.test :refer :all]
            [megafono-feed.views.helpers.xml :refer :all]))

(deftest strip-html-test
  (testing "strip-html"
           (is (= "Hello"
                  (strip-html "<div><p>Hello</p></div>")))))

(deftest tag-test
  (testing "macro tag"
           (is (= {:content ["Title"], :attrs {:href "/title"}, :tag :title}
                  (tag :title {:href "/title"} "Title")))))

(deftest category-tag-test
  (testing "category-tag"
           (is (= {:content [], :attrs {:text "Bussiness"}, :tag :itunes:category}
                  (category-tag {:name "Bussiness"})))))

(deftest build-category-tag-test
  (testing "build-category-tag"
           (is (= [{:content [], :attrs {:text "Podcasting"}, :tag :itunes:category}]
                  (build-category-tag [nil [{:name "Podcasting"}]])))))

(deftest categories-tag-test
  (testing "categories-tag"
           (is (= [{:content [], :attrs {:text "Podcasting"}, :tag :itunes:category}]
                  (categories-tag [{:name "Podcasting"}])))))