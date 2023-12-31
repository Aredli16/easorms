import type { Metadata } from 'next';
import { ReactNode } from 'react';

export const metadata: Metadata = {
  title: 'Create Next App',
  description: 'Generated by create next app',
};

export default function RootLayout({ children }: { children: ReactNode; }) {
  return (
    <html lang="fr" className="h-full">
    <body className="h-full">{children}</body>
    </html>
  );
}
