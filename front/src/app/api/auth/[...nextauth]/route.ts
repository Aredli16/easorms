import NextAuth, { AuthOptions } from 'next-auth';
import KeycloakProvider from 'next-auth/providers/keycloak';
import { jwtDecode } from 'jwt-decode';
import { fetch } from 'next/dist/compiled/@edge-runtime/primitives';

export const authOption: AuthOptions = {
  providers: [
    KeycloakProvider({
      clientId: `${process.env.KEYCLOAK_CLIENT_ID}`,
      clientSecret: `${process.env.KEYCLOAK_CLIENT_SECRET}`,
      issuer: `${process.env.KEYCLOAK_ISSUER}`,
    }),
  ],
  callbacks: {
    async jwt({ token, account }) {
      if (account) {
        // This is the first time the user signs in
        token.id_token = account.id_token!;
        token.access_token = account.access_token!;
        token.refresh_token = account.refresh_token!;
        token.expires_at = account.expires_at!;
        token.decodedToken = jwtDecode(account.access_token!);
      } else if (Math.floor(Date.now() / 1000) > token.expires_at) {
        console.log('Refreshing token');
        // The user has been signed in before
        // But the token is about to expire;
        const response = await fetch(`${process.env.KEYCLOAK_ISSUER}/protocol/openid-connect/token`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
          body: new URLSearchParams({
            grant_type: 'refresh_token',
            client_id: `${process.env.KEYCLOAK_CLIENT_ID}`,
            client_secret: `${process.env.KEYCLOAK_CLIENT_SECRET}`,
            refresh_token: token.refresh_token,
          }),
        });

        if (!response.ok) {
          await fetch(`${process.env.KEYCLOAK_ISSUER}/protocol/openid-connect/logout?id_token_hint=${token.id_token}`);
          throw new Error('Failed to refresh token');
        }

        const refreshedTokens = await response.json();

        token.id_token = refreshedTokens.id_token;
        token.access_token = refreshedTokens.access_token;
        token.refresh_token = refreshedTokens.refresh_token;
        token.expires_at = Math.floor(Date.now() / 1000) + refreshedTokens.expires_in;
        token.decodedToken = jwtDecode(refreshedTokens.access_token);
      }

      return token;
    },
    async session({ session, token }) {
      // Send properties to the client.
      session.access_token = token.access_token;
      session.isAdmin = token.decodedToken.realm_access.roles.includes('admin');
      session.emailVerified = token.decodedToken.email_verified;

      return session;
    },
  },
  events: {
    signOut: async ({ token }) => {
      await fetch(`${process.env.KEYCLOAK_ISSUER}/protocol/openid-connect/logout?id_token_hint=${token.id_token}`);
    },
  },
  pages: {
    signIn: '/auth',
  },
};

export const handler = NextAuth(authOption);

export { handler as GET, handler as POST };
