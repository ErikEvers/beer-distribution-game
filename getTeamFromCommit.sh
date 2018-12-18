#!/bin/bash

commit_sha=$(git rev-parse HEAD)

author=$(git log --format='%ae' $commit_sha^!)

case $author in
    CAGG.Gerrits@student.han.nl)
        team="1"
        ;;
    RG.Zweers@student.han.nl)
        team="1"
        ;;
    R.Grobbee@student.han.nl)
        team="2"
        ;;
    r.grobbee@gmail.com)
        team="2"
        ;;
    MR.Bouwmeister@student.han.nl)
        team="2"
        ;;
    JJ.Dimmendaal@student.han.nl)
        team="3"
        ;;
    BM.Janssen1@student.han.nl)
        team="4"
        ;;
    GAW.vanBergen@student.han.nl)
        team="2"
        ;;
    DJ.Talahatu@student.han.nl)
        team="4"
        ;;
    PG.Wobben@student.han.nl)
        team="4"
        ;;
    E.Evers1@student.han.nl)
        team="4"
        ;;
    TZ.Bal@student.han.nl)
        team="2"
        ;;
    PJM.Heij@student.han.nl)
        team="1"
        ;;
    OT.Katsma@student.han.nl)
        team="2"
        ;;
    MD.Kortsilius@student.han.nl)
        team="3"
        ;;
    K.vandeWetering@student.han.nl)
        team="5"
        ;;
    M.Hulshof@student.han.nl)
        team="5"
        ;;
    J.Veldhuizen@student.han.nl)
        team="3"
        ;;
    S.Timmers@student.han.nl)
        team="4"
        ;;
    JQ.vanWolfwinkel@student.han.nl)
        team="1"
        ;;
    C.Flokstra@student.han.nl)
        team="3"
        ;;
    YF.Boelens@student.han.nl)
        team="5"
        ;;
    JL.vanEngen@student.han.nl)
        team="1"
        ;;
    R.deBruine1@student.han.nl)
        team="5"
        ;;
esac

echo $team