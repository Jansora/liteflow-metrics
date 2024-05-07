
/** @type {import('next').NextConfig} */
const nextConfig = {

    webpack: (config) => {
      config.resolve.alias.canvas = false;

        // config.resolve.alias = Object.assign({}, config.resolve.alias, {
        //     "react-pdf": "react-pdf/dist/entry.noworker.js"
        // });
        // You may not need this, it's just to support moduleResolution: 'node16'
        config.resolve.extensionAlias = {
            '.js': ['.js', '.ts', '.tsx'],
        };
        return config;
    },

    output: 'standalone',
    // swcMinify: false,
    images: {
        remotePatterns: [
            {
                protocol: 'https',
                hostname: '*',
                port: '',
                pathname: '/**',
            },
        ],
    },
    // experimental: {
    //     esmExternals: 'loose'
    // }
}

module.exports = nextConfig
