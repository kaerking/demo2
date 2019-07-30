# demo2
[{rabbit, [ {tcp_listeners, [5672] },
             {ssl_listeners, [5671] },
             {ssl_options, [
               {cacertfile, "cacert.pem" },
               {certfile, "rabbit3.cert.pem" },
               {keyfile, "rabbit3.key.pem" },
               {verify, verify_peer},
               {fail_if_no_peer_cert, false },
                {versions, ['tlsv1.2', 'tlsv1.1']}
        ]}
  ]}
].
