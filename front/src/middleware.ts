import { withAuth } from 'next-auth/middleware';
import { NextResponse } from 'next/server';

export default withAuth(
  async request => {
    if (request.nextUrl.pathname.startsWith('/admin') && !request.nextauth.token?.decodedToken.realm_access.roles.includes('admin')) {
      return NextResponse.rewrite(new URL('/not-found', request.url));
    }
  },
  {
    callbacks: {
      authorized: ({ token }) => !!token,
    },
  },
);

export const config = { matcher: ['/admin/:path*'] };
