#! /bin/bash

DIR="$(cd $(dirname $0) ; pwd -P)"

. $DIR/demo.config

if [ -z "$TOKEN" ]; then
    echo "Configure \$TOKEN" >&2
    exit 1
fi

if [ -z "$HOST" ]; then
    echo "Configure \$HOST" >&2
    exit 1
fi

set -eu

function remote() {
  ssh -l root $HOST "$@"
}

remote "
  apt update
  apt install -y curl
  curl -Ls https://github.com/squat/kilo/releases/download/0.5.0/kgctl-linux-amd64 > /usr/local/bin/kgctl
  chmod 755 /usr/local/bin/kgctl
  which kgctl
  kgctl --version
"

remote "
  ufw disable
  apt-get purge -y ufw
"

remote "curl -sfL https://get.k3s.io | K3S_TOKEN=$TOKEN sh -s - server --tls-san $HOST --disable-helm-controller --disable=traefik --disable=metrics-server --disable=local-storage"

remote "cat /etc/rancher/k3s/k3s.yaml" | sed "s/127\\.0\\.0\\.1/$HOST/g" > kubeconfig.yaml