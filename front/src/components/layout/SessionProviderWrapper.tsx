'use client';

import { ReactNode } from 'react';
import { SessionProvider } from 'next-auth/react';

const SessionProviderWrapper = ({ children }: { children: ReactNode }) => {
  return (
    <SessionProvider>
      {children}
    </SessionProvider>
  );
};

export default SessionProviderWrapper;
