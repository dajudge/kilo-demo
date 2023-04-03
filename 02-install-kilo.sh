#! /bin/bash

kubectl apply -f https://raw.githubusercontent.com/squat/kilo/0.5.0/manifests/crds.yaml
kubectl apply -f https://raw.githubusercontent.com/squat/kilo/0.5.0/manifests/kilo-k3s.yaml