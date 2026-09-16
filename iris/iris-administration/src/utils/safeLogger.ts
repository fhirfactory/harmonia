/*
 * Copyright (c) 2026 Mark Hunter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/**
 * Safe Browser Logger adhering to Harmonia Zero-PHI and Secret Privacy Policy.
 * Redacts sensitive fields, tokens, passwords, and identifiers from browser console.
 */
class SafeLogger {
  private prefix = '[Harmonia Iris-Admin]';

  private sanitize(data: any): any {
    if (data === null || data === undefined) return data;
    if (typeof data === 'string') {
      // Redact potential Bearer tokens and JWTs
      return data
        .replace(/Bearer\s+[A-Za-z0-9-_=]+\.[A-Za-z0-9-_=]+\.?[A-Za-z0-9-_.+/=]*/gi, 'Bearer [REDACTED_JWT]')
        .replace(/password\s*[:=]\s*["']?[^,"'\s]+["']?/gi, 'password:[REDACTED]');
    }
    if (typeof data !== 'object') return data;

    if (Array.isArray(data)) {
      return data.map(item => this.sanitize(item));
    }

    const sanitized: Record<string, any> = {};
    const sensitiveKeys = [
      'password', 'secret', 'token', 'jwt', 'authorization', 'bearer',
      'ssn', 'taxId', 'medicare', 'privateKey', 'creditCard'
    ];

    for (const [key, value] of Object.entries(data)) {
      if (sensitiveKeys.some(s => key.toLowerCase().includes(s))) {
        sanitized[key] = '[REDACTED_SECRET]';
      } else if (typeof value === 'object') {
        sanitized[key] = this.sanitize(value);
      } else {
        sanitized[key] = value;
      }
    }
    return sanitized;
  }

  log(message: string, context?: Record<string, any>) {
    console.log(`${this.prefix} ${message}`, context ? this.sanitize(context) : '');
  }

  info(message: string, context?: Record<string, any>) {
    console.info(`${this.prefix} [INFO] ${message}`, context ? this.sanitize(context) : '');
  }

  warn(message: string, context?: Record<string, any>) {
    console.warn(`${this.prefix} [WARN] ${message}`, context ? this.sanitize(context) : '');
  }

  error(message: string, error?: any, context?: Record<string, any>) {
    console.error(
      `${this.prefix} [ERROR] ${message}`,
      error?.message || error || '',
      context ? this.sanitize(context) : ''
    );
  }
}

export const safeLogger = new SafeLogger();
