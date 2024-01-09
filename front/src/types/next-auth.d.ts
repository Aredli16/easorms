import { DefaultJWT } from 'next-auth/jwt';
import { DefaultSession } from 'next-auth';

declare module 'next-auth' {
  interface Session extends DefaultSession {
    access_token: string;
    isAdmin: boolean;
    emailVerified: boolean;
  }
}

declare module 'next-auth/jwt' {
  interface JWT extends DefaultJWT {
    id_token: string;
    access_token: string;
    refresh_token: string;
    expires_at: number;
    decodedToken: {
      realm_access: {
        roles: string[];
      };
      email_verified: boolean;
    };
  }
}
